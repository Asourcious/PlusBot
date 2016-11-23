package org.asourcious.plusbot.config;

import net.dv8tion.jda.core.utils.SimpleLog;
import org.apache.commons.math3.util.Pair;
import org.asourcious.plusbot.Constants;

import java.sql.*;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public abstract class DataSource<T> {

    public static final SimpleLog LOG = SimpleLog.getLog("Database");

    protected Connection connection;
    private ExecutorService executorService;

    protected String table;
    protected Map<String, Set<T>> cache;

    protected PreparedStatement add;
    protected PreparedStatement remove;
    protected PreparedStatement clear;

    public DataSource(Connection connection, ExecutorService executorService, String table) throws SQLException {
        this.connection = connection;
        this.executorService = executorService;
        this.table = table;
    }

    public void load() {
        cache = new ConcurrentHashMap<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table);

            while (resultSet.next()) {
                Pair<String, T> entry = parseRow(toArray(resultSet));

                if (!cache.containsKey(entry.getKey()))
                    cache.put(entry.getKey(), ConcurrentHashMap.newKeySet());

                cache.get(entry.getKey()).add(entry.getValue());
            }
        } catch (SQLException ex) {
            LOG.log(ex);
            System.exit(Constants.DATABASE_ERROR);
        }
    }

    public boolean has(String container, T entry) {
        return cache.containsKey(container) && cache.get(container).contains(entry);
    }

    public Set<T> get(String container) {
        if (!cache.containsKey(container))
            return Collections.emptySet();

        return Collections.unmodifiableSet(cache.get(container));
    }

    public void add(String container, T entry) {
        if (!cache.containsKey(container))
            cache.put(container, ConcurrentHashMap.newKeySet());

        cache.get(container).add(entry);
        executeStatement(add, toArgs(container, entry));
    }

    public void remove(String container, T entry) {
        cache.get(container).remove(entry);
        executeStatement(remove, toArgs(container, entry));
    }

    public void clear(String container) {
        cache.get(container).clear();
        executeStatement(clear, container);
    }

    protected abstract Pair<String, T> parseRow(String[] columns);
    protected abstract String[] toArgs(String container, T entry);

    private void executeStatement(PreparedStatement statement, String... args) {
        executorService.execute(() -> {
            try {
                for (int i = 0; i < args.length; i++) {
                    statement.setString(i + 1, args[i]);
                }
                statement.execute();
            } catch (SQLException ex) {
                LOG.log(ex);
            }
        });
    }

    private String[] toArray(ResultSet results) throws SQLException {
        String[] result = new String[results.getMetaData().getColumnCount()];

        for (int i = 0; i < result.length; i++) {
            result[i] = results.getString(i + 1);
        }

        return result;
    }
}

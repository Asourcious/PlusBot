package org.asourcious.plusbot.config;

import net.dv8tion.jda.core.utils.SimpleLog;

import java.sql.*;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class DataSource {

    public static final SimpleLog LOG = SimpleLog.getLog("Database");

    protected Connection connection;
    private ExecutorService executorService;

    protected String table;
    protected Map<String, Set<String>> cache;

    protected PreparedStatement add;
    protected PreparedStatement remove;
    protected PreparedStatement clear;

    public DataSource(Connection connection, String table, ExecutorService executorService) throws SQLException {
        this.connection = connection;
        this.executorService = executorService;
        this.table = table;

        this.add = connection.prepareStatement("INSERT INTO " + table + " (container, entry) VALUES (?, ?);");
        this.remove = connection.prepareStatement("DELETE FROM " + table + " WHERE container = ? AND entry = ?;");
        this.clear = connection.prepareStatement("DELETE FROM " + table + " WHERE container = ?;");
    }

    public DataSource load() {
        cache = new ConcurrentHashMap<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table);

            while (resultSet.next()) {
                if (!cache.containsKey(resultSet.getString(2)))
                    cache.put(resultSet.getString(2), ConcurrentHashMap.newKeySet());

                cache.get(resultSet.getString(2)).add(resultSet.getString(3));
            }
        } catch (SQLException ex) {
            LOG.log(ex);
            System.exit(1);
        }

        return this;
    }

    public boolean has(String container, String entry) {
        return cache.containsKey(container) && cache.get(container).contains(entry);
    }

    public Set<String> get(String container) {
        if (!cache.containsKey(container))
            return Collections.emptySet();

        return Collections.unmodifiableSet(cache.get(container));
    }

    public void add(String container, String entry) {
        if (!cache.containsKey(container))
            cache.put(container, ConcurrentHashMap.newKeySet());

        cache.get(container).add(entry);
        executeStatement(add, container, entry);
    }

    public void remove(String container, String entry) {
        cache.get(container).remove(entry);
        executeStatement(remove, container, entry);
    }

    public void clear(String container) {
        cache.get(container).clear();
        executeStatement(clear, container);
    }

    protected void executeStatement(PreparedStatement statement, String... args) {
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
}

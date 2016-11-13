package org.asourcious.plusbot.handle.database;

import java.sql.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TableHandler {

    private String table;
    private Connection connection;
    private PreparedStatement add;
    private PreparedStatement remove;
    private PreparedStatement clear;

    public TableHandler(Connection connection, String table) throws SQLException {
        this.connection = connection;
        this.table = table;
        this.add = connection.prepareStatement("INSERT INTO " + table + " (container, entry) VALUES (?, ?);");
        this.remove = connection.prepareStatement("DELETE FROM " + table + " WHERE container = ? AND entry = ?;");
        this.clear = connection.prepareStatement("DELETE FROM " + table + " WHERE container = ?;");
    }

    public Set<String> loadTable(String container) {
        Set<String> cache = ConcurrentHashMap.newKeySet();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE container = " + container);

            while (resultSet.next()) {
                cache.add(resultSet.getString(3));
            }

            return cache;
        } catch (SQLException ex) {
            DatabaseController.LOG.log(ex);
            System.exit(1);
        }
        return null;
    }

    public void addEntry(String container, String entry) {
        executeStatement(add, container, entry);
    }

    public void removeEntry(String container, String entry) {
        executeStatement(remove, container, entry);
    }

    public void clearEntries(String container) {
        try {
            clear.setString(1, container);
            clear.execute();
        } catch (SQLException ex) {
            DatabaseController.LOG.log(ex);
        }
    }

    private void executeStatement(PreparedStatement statement, String container, String entry) {
        try {
            statement.setString(1, container);
            statement.setString(2, entry);
            statement.execute();
        } catch (SQLException ex) {
            DatabaseController.LOG.log(ex);
        }
    }
}
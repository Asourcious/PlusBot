package org.asourcious.plusbot.config.source;

import org.apache.commons.math3.util.Pair;
import org.asourcious.plusbot.Constants;
import org.asourcious.plusbot.config.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

public class GuildDisabledCommands extends DataSource<String> {

    public GuildDisabledCommands(Connection connection, ExecutorService executorService) throws SQLException {
        super(connection, executorService, Constants.GUILD_DISABLED_COMMANDS);
        this.add    = connection.prepareStatement("INSERT INTO " + table + " (container, entry) VALUES (?, ?);");
        this.remove = connection.prepareStatement("DELETE FROM " + table + " WHERE container = ? AND entry = ?;");
        this.clear  = connection.prepareStatement("DELETE FROM " + table + " WHERE container = ?;");
    }

    @Override
    protected Pair<String, String> parseRow(String[] columns) {
        return new Pair<>(columns[1], columns[2]);
    }

    @Override
    protected String[] toArgs(String container, String entry) {
        return new String[] { container, entry };
    }
}

package org.asourcious.plusbot.config.source;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.math3.util.Pair;
import org.asourcious.plusbot.Constants;
import org.asourcious.plusbot.config.DataSource;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

public class GuildDisabledCommands extends DataSource<String> {

    public GuildDisabledCommands(HikariDataSource connectionPool, ExecutorService executorService) throws SQLException {
        super(connectionPool, executorService, Constants.GUILD_DISABLED_COMMANDS);
        this.add    = "INSERT INTO " + table + " (container, entry) VALUES (?, ?);";
        this.remove = "DELETE FROM " + table + " WHERE container = ? AND entry = ?;";
        this.clear  = "DELETE FROM " + table + " WHERE container = ?;";
    }

    @Override
    protected Pair<String, String> deserializeRow(String[] columns) {
        return new Pair<>(columns[1], columns[2]);
    }

    @Override
    protected String[] serializeRow(String container, String entry) {
        return new String[] { container, entry };
    }
}

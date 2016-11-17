package org.asourcious.plusbot.config;

import org.asourcious.plusbot.Constants;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Settings {

    private JSONObject credentials;

    private ExecutorService executorService;

    private DataSource autoBotRoles;
    private DataSource autoHumanRoles;
    private DataSource blacklist;
    private DataSource channelDisabledCommands;
    private DataSource guildDisabledCommands;
    private DataSource prefixes;

    public Settings() throws IOException {
        this.credentials = new JSONObject(new String(Files.readAllBytes(Paths.get("credentials.json"))));
        this.executorService = Executors.newSingleThreadExecutor();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/plusbot?autoReconnect=true&useSSL=false&serverTimezone=UTC", credentials.getString("user"), credentials.getString("pass"));

            autoBotRoles = new DataSource(connection, Constants.AUTOROLE_BOT).load();
            autoHumanRoles = new DataSource(connection, Constants.AUTOROLE_HUMAN).load();
            blacklist = new DataSource(connection, Constants.BLACKLIST).load();
            channelDisabledCommands = new DataSource(connection, Constants.CHANNEL_DISABLED_COMMANDS).load();
            guildDisabledCommands = new DataSource(connection, Constants.GUILD_DISABLED_COMMANDS).load();
            prefixes = new DataSource(connection, Constants.PREFIXES).load();
        } catch (SQLException e) {
            DataSource.LOG.log(e);
            System.exit(1);
        }
    }

    public String getToken() {
        return credentials.getString("token");
    }

    public DataSource getAutoBotRoles() {
        return autoBotRoles;
    }

    public DataSource getAutoHumanRoles() {
        return autoHumanRoles;
    }

    public DataSource getBlacklist() {
        return blacklist;
    }

    public DataSource getChannelDisabledCommands() {
        return channelDisabledCommands;
    }

    public DataSource getGuildDisabledCommands() {
        return guildDisabledCommands;
    }

    public DataSource getPrefixes() {
        return prefixes;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}

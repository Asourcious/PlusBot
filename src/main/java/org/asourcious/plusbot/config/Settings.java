package org.asourcious.plusbot.config;

import net.dv8tion.jda.core.entities.Guild;
import org.asourcious.plusbot.Constants;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Settings {

    private JSONObject credentials;

    private Connection connection;
    private ExecutorService executorService;

    private Map<String, GuildProfile> guildProfiles;
    private DataSource blacklist;
    private DataSource channelDisabledCommands;
    private DataSource guildDisabledCommands;
    private DataSource prefixes;

    public Settings() throws IOException {
        this.credentials = new JSONObject(new String(Files.readAllBytes(Paths.get("credentials.json"))));
        this.guildProfiles = new ConcurrentHashMap<>();
        this.executorService = Executors.newSingleThreadExecutor();

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/plusbot?autoReconnect=true&useSSL=false&serverTimezone=UTC", credentials.getString("user"), credentials.getString("pass"));

            blacklist = new DataSource(connection, Constants.BLACKLIST, executorService).load();
            channelDisabledCommands = new DataSource(connection, Constants.CHANNEL_DISABLED_COMMANDS, executorService).load();
            guildDisabledCommands = new DataSource(connection, Constants.GUILD_DISABLED_COMMANDS, executorService).load();
            prefixes = new DataSource(connection, Constants.PREFIXES, executorService).load();
        } catch (SQLException e) {
            DataSource.LOG.log(e);
            System.exit(1);
        }
    }

    public String getToken() {
        return credentials.getString("token");
    }

    public GuildProfile getProfile(Guild guild) {
        if (!guildProfiles.containsKey(guild.getId()))
            guildProfiles.put(guild.getId(), new GuildProfile(guild.getId(), connection, executorService).load());

        return guildProfiles.get(guild.getId());
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

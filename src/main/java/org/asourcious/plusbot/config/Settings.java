package org.asourcious.plusbot.config;

import net.dv8tion.jda.core.entities.Guild;
import org.asourcious.plusbot.config.source.Blacklists;
import org.asourcious.plusbot.config.source.ChannelDisabledCommands;
import org.asourcious.plusbot.config.source.GuildDisabledCommands;
import org.asourcious.plusbot.config.source.Prefixes;
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
    private Blacklists blacklists;
    private ChannelDisabledCommands channelDisabledCommands;
    private GuildDisabledCommands guildDisabledCommands;
    private Prefixes prefixes;

    public Settings() throws IOException {
        this.credentials = new JSONObject(new String(Files.readAllBytes(Paths.get("credentials.json"))));
        this.guildProfiles = new ConcurrentHashMap<>();
        this.executorService = Executors.newSingleThreadExecutor();

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/plusbot?autoReconnect=true&useSSL=false&serverTimezone=UTC", credentials.getString("user"), credentials.getString("pass"));

            blacklists = new Blacklists(connection, executorService);
            channelDisabledCommands = new ChannelDisabledCommands(connection, executorService);
            guildDisabledCommands = new GuildDisabledCommands(connection, executorService);
            prefixes = new Prefixes(connection, executorService);

            executorService.execute(() -> {
                DataSource.LOG.info("Loading persistent data...");
                blacklists.load();
                channelDisabledCommands.load();
                guildDisabledCommands.load();
                prefixes.load();
                DataSource.LOG.info("Loading complete.");
            });
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

    public Blacklists getBlacklists() {
        return blacklists;
    }

    public ChannelDisabledCommands getChannelDisabledCommands() {
        return channelDisabledCommands;
    }

    public GuildDisabledCommands getGuildDisabledCommands() {
        return guildDisabledCommands;
    }

    public Prefixes getPrefixes() {
        return prefixes;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}

package org.asourcious.plusbot.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.dv8tion.jda.core.entities.Guild;
import org.asourcious.plusbot.Constants;
import org.asourcious.plusbot.config.source.*;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Settings {

    private JSONObject credentials;

    private HikariDataSource connectionPool;
    private ExecutorService executorService;

    private Map<String, GuildProfile> guildProfiles;
    private Blacklists blacklists;
    private ChannelDisabledCommands channelDisabledCommands;
    private GuildDisabledCommands guildDisabledCommands;
    private Mutes mutes;
    private Prefixes prefixes;
    private GuildTags guildTags;

    public Settings() throws IOException {
        this.credentials = new JSONObject(new String(Files.readAllBytes(Paths.get("credentials.json"))));
        this.guildProfiles = new ConcurrentHashMap<>();
        this.executorService = Executors.newSingleThreadExecutor();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost/plusbot");
        config.setPoolName("Connection Pool");
        config.setUsername(credentials.getString("user"));
        config.setPassword(credentials.getString("pass"));
        config.addDataSourceProperty("useSSL", "false");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        this.connectionPool = new HikariDataSource(config);
        try {
            blacklists = new Blacklists(connectionPool, executorService);
            channelDisabledCommands = new ChannelDisabledCommands(connectionPool, executorService);
            guildDisabledCommands = new GuildDisabledCommands(connectionPool, executorService);
            mutes = new Mutes(connectionPool, executorService);
            prefixes = new Prefixes(connectionPool, executorService);
            guildTags = new GuildTags(connectionPool, executorService);

            executorService.execute(() -> {
                DataSource.LOG.info("Loading persistent data...");
                blacklists.load();
                channelDisabledCommands.load();
                guildDisabledCommands.load();
                mutes.load();
                prefixes.load();
                guildTags.load();
                DataSource.LOG.info("Loading complete.");
            });
        } catch (SQLException ex) {
            DataSource.LOG.error("An exception occurred", ex);
            System.exit(Constants.DATABASE_ERROR);
        }
    }

    public String getDiscordToken() {
        return credentials.getString("discord");
    }

    public String getMashapeToken() {
        return credentials.getString("mashape");
    }

    public String getWeatherToken() {
        return credentials.getString("weather");
    }

    public GuildProfile getProfile(Guild guild) {
        if (!guildProfiles.containsKey(guild.getId()))
            guildProfiles.put(guild.getId(), new GuildProfile(guild.getId(), connectionPool, executorService).load());

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

    public Mutes getMutes() {
        return mutes;
    }

    public Prefixes getPrefixes() {
        return prefixes;
    }

    public GuildTags getGuildTags() {
        return guildTags;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}

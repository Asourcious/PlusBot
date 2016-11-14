package org.asourcious.plusbot.config;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.asourcious.plusbot.handle.database.DatabaseController;
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

    private Map<String, Configuration> guildConfigurations;
    private Map<String, Configuration> channelConfigurations;

    public Settings() throws IOException {
        this.credentials = new JSONObject(new String(Files.readAllBytes(Paths.get("credentials.json"))));
        this.executorService = Executors.newSingleThreadExecutor();
        this.guildConfigurations = new ConcurrentHashMap<>();
        this.channelConfigurations = new ConcurrentHashMap<>();

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/plusbot?autoReconnect=true&useSSL=false&serverTimezone=UTC", credentials.getString("user"), credentials.getString("pass"));
        } catch (SQLException e) {
            DatabaseController.LOG.log(e);
            System.exit(1);
        }
    }

    public String getToken() {
        return credentials.getString("token");
    }

    public Configuration getConfiguration(Guild guild) {
        if (!guildConfigurations.containsKey(guild.getId()))
            guildConfigurations.put(guild.getId(), new Configuration(guild, connection, executorService));

        return guildConfigurations.get(guild.getId());
    }

    public Configuration getConfiguration(MessageChannel channel) {
        if (!channelConfigurations.containsKey(channel.getId()))
            channelConfigurations.put(channel.getId(), new Configuration(channel, connection, executorService));

        return channelConfigurations.get(channel.getId());
    }

    public void shutdown() {
        executorService.shutdown();
    }
}

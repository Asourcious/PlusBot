package org.asourcious.plusbot.config;

import org.apache.commons.lang3.ArrayUtils;
import org.asourcious.plusbot.Constants;

import java.sql.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;

public class GuildProfile {

    public static int BOT_ROLE = 0;
    public static int HUMAN_ROLE = 1;
    //public static int MOD_LOG_CHANNEL = 2;
    public static int WELCOME_CHANNEL = 3;
    public static int WELCOME_MESSAGE = 4;
    public static int WELCOME_DM_MESSAGE = 5;

    private Connection connection;
    private ExecutorService executorService;

    private String guild;
    private String[] properties;

    public GuildProfile(String guild, Connection connection, ExecutorService executorService) {
        this.guild = guild;
        this.connection = connection;
        this.executorService = executorService;
    }

    public GuildProfile load() {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + Constants.GUILD_PROFILES + " WHERE guild_id = '" + guild + "'");
            properties = new String[resultSet.getMetaData().getColumnCount() - 2];
            if (!resultSet.next())
                return this;

            for (int i = 0; i < properties.length; i++) {
                properties[i] = resultSet.getString(i + 2);
            }
        } catch (SQLException e) {
            DataSource.LOG.log(e);
        }
        return this;
    }

    public void update() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM " + Constants.GUILD_PROFILES + " WHERE guild_id = '" + guild + "'");
            run(connection.prepareStatement("INSERT INTO " + Constants.GUILD_PROFILES + " VALUES " + toArgs()), ArrayUtils.addAll(new String[] {null, guild}, properties));
        } catch (SQLException e) {
            DataSource.LOG.log(e);
        }
    }

    public String getProperty(int property) {
        return properties[property];
    }

    public boolean hasProperty(int property) {
        return getProperty(property) != null;
    }

    public void setProperty(int property, String value) {
        properties[property] = value;
        executorService.execute(this::update);
    }

    public void removeProperty(int property) {
        properties[property] = null;
        executorService.execute(this::update);
    }

    private void run(PreparedStatement statement, String... args) {
        try {
            for (int i = 0; i < args.length; i++) {
                statement.setString(i + 1, args[i]);
            }
            statement.execute();
        } catch (SQLException ex) {
            DataSource.LOG.log(ex);
        }
    }

    private String toArgs() {
        String args = "(?, ";
        for (int i = 0; i < properties.length; i++) args += "?, ";
        return args + "?);";
    }
}

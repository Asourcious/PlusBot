package org.asourcious.plusbot.config;

import com.zaxxer.hikari.HikariDataSource;
import org.asourcious.plusbot.Constants;

import java.sql.*;
import java.util.concurrent.ExecutorService;

public class GuildProfile {

    public static int BOT_ROLE = 0;
    public static int HUMAN_ROLE = 1;
    //public static int MOD_LOG_CHANNEL = 2;
    public static int WELCOME_CHANNEL = 3;
    public static int WELCOME_MESSAGE = 4;
    public static int WELCOME_DM_MESSAGE = 5;

    private HikariDataSource connectionPool;
    private ExecutorService executorService;

    private String guild;
    private String[] properties;

    public GuildProfile(String guild, HikariDataSource connectionPool, ExecutorService executorService) {
        this.guild = guild;
        this.connectionPool = connectionPool;
        this.executorService = executorService;
    }

    public GuildProfile load() {
        try (Connection connection = connectionPool.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + Constants.GUILD_PROFILES + " WHERE guild_id = '" + guild + "'");
            properties = new String[resultSet.getMetaData().getColumnCount() - 2];
            if (!resultSet.next())
                return this;

            for (int i = 0; i < properties.length; i++) {
                properties[i] = resultSet.getString(i + 3);
            }
        } catch (SQLException e) {
            DataSource.LOG.log(e);
        }

        return this;
    }

    public void update() {
        try (Connection connection = connectionPool.getConnection()) {
            Statement remove = connection.createStatement();
            remove.execute("DELETE FROM " + Constants.GUILD_PROFILES + " WHERE guild_id = '" + guild + "'");
            PreparedStatement insert = getUpdateStatement(connection);
            insert.execute();
        } catch (SQLException e) {
            DataSource.LOG.log(e);
        }
    }

    public String getProperty(int property) {
        if (properties == null)
            return null;

        return properties[property];
    }

    public boolean hasProperty(int property) {
        return getProperty(property) != null;
    }

    public void setProperty(int property, String value) {
        if (properties == null)
            return;

        properties[property] = value;
        executorService.execute(this::update);
    }

    public void removeProperty(int property) {
        if (properties == null)
            return;

        properties[property] = null;
        executorService.execute(this::update);
    }

    private String toArgs() {
        String args = "(?, ";
        for (String ignored : properties) args += "?, ";
        return args + "?);";
    }

    private PreparedStatement getUpdateStatement(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO " + Constants.GUILD_PROFILES + " VALUES " + toArgs());

        statement.setString(1, null);
        statement.setString(2, guild);
        for (int i = 0; i < properties.length; i++) {
            statement.setString(i + 3, properties[i]);
        }

        return statement;
    }
}

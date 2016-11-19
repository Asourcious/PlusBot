package org.asourcious.plusbot.config;

import org.asourcious.plusbot.Constants;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;

public class GuildProfile {

    private Connection connection;
    private ExecutorService executorService;

    private String guild;

    private String autoBotRole;
    private String autoHumanRole;
    private String modLogChannel;
    private String welcomeChannel;

    public GuildProfile(String guild, Connection connection, ExecutorService executorService) {
        this.guild = guild;
        this.connection = connection;
        this.executorService = executorService;
    }

    public GuildProfile load() {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + Constants.GUILD_PROFILES + " WHERE guild_id = '" + guild + "'");
            if (!resultSet.next())
                return this;

            autoBotRole = resultSet.getString(2);
            autoHumanRole = resultSet.getString(3);
            modLogChannel = resultSet.getString(4);
            welcomeChannel = resultSet.getString(5);
        } catch (SQLException e) {
            DataSource.LOG.log(e);
        }
        return this;
    }

    public void update() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM " + Constants.GUILD_PROFILES + " WHERE guild_id = '" + guild + "'");
            statement.execute("INSERT INTO " + Constants.GUILD_PROFILES + " (guild_id, bot_role, human_role, mod_log_channel, welcome_channel) VALUES ('"
                    + guild + "', '" + autoBotRole + "', '" + autoHumanRole + "', '" + modLogChannel + "', '" + welcomeChannel + "')");
        } catch (SQLException e) {
            DataSource.LOG.log(e);
        }
    }

    public String getAutoBotRole() {
        return autoBotRole;
    }

    public void setAutoBotRole(String autoBotRole) {
        this.autoBotRole = autoBotRole;
        executorService.execute(this::update);
    }

    public void removeAutoBotRole() {
        autoBotRole = null;
        executorService.execute(this::update);
    }

    public String getAutoHumanRole() {
        return autoHumanRole;
    }

    public void setAutoHumanRole(String autoHumanRole) {
        this.autoHumanRole = autoHumanRole;
        executorService.execute(this::update);
    }

    public void removeAutoHumanRole() {
        autoHumanRole = null;
        executorService.execute(this::update);
    }

    public String getModLogChannel() {
        return modLogChannel;
    }

    public void setModLogChannel(String modLogChannel) {
        this.modLogChannel = modLogChannel;
        executorService.execute(this::update);
    }

    public void removeModLogChannel() {
        modLogChannel = null;
        executorService.execute(this::update);
    }

    public String getWelcomeChannel() {
        return welcomeChannel;
    }

    public void setWelcomeChannel(String welcomeChannel) {
        this.welcomeChannel = welcomeChannel;
        executorService.execute(this::update);
    }

    public void removeWelcomeChannel() {
        welcomeChannel = null;
        executorService.execute(this::update);
    }
}

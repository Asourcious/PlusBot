package org.asourcious.plusbot.config;

import net.dv8tion.jda.core.entities.ISnowflake;
import org.asourcious.plusbot.handle.database.DatabaseController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class Configuration {

    private ExecutorService executor;

    private DatabaseController databaseController;

    private Set<String> blacklist;
    private Set<String> disabledCommands;
    private Set<String> prefixes;
    private Set<String> humanAutoRoles;
    private Set<String> botAutoRoles;

    public Configuration(ISnowflake entity, Connection connection, ExecutorService executor) {
        this.executor = executor;

        try {
            this.databaseController = new DatabaseController(connection, entity);

            this.blacklist = databaseController.loadBlacklist();
            this.disabledCommands = databaseController.loadGuildDisabledCommands();
            this.prefixes = databaseController.loadPrefixes();
            this.humanAutoRoles = databaseController.loadHumanAutoRoles();
            this.botAutoRoles = databaseController.loadBotAutoRoles();
        } catch (SQLException ex) {
            DatabaseController.LOG.log(ex);
            System.exit(1);
        }
    }

    public Set<String> getBlacklist() {
        return Collections.unmodifiableSet(new HashSet<>(blacklist));
    }

    public void addUserToBlacklist(String userID) {
        blacklist.add(userID);
        executor.execute(() -> databaseController.addUserToBlacklist(userID));
    }

    public void removeUserFromBlacklist(String userID) {
        blacklist.remove(userID);
        executor.execute(() -> databaseController.removeUserFromBlacklist(userID));
    }

    public void clearBlacklist() {
        blacklist.clear();
        executor.execute(() -> databaseController.clearBlacklist());
    }

    public Set<String> getDisabledCommands() {
        return Collections.unmodifiableSet(new HashSet<>(disabledCommands));
    }

    public void addDisabledCommand(String command) {
        disabledCommands.add(command);
        executor.execute(() -> databaseController.addDisabledCommand(command));
    }

    public void removeDisabledCommand(String command) {
        disabledCommands.remove(command);
        executor.execute(() -> databaseController.removeDisabledCommand(command));
    }

    public void clearDisabledCommands() {
        disabledCommands.clear();
        executor.execute(() -> databaseController.clearDisabledCommands());
    }

    public Set<String> getPrefixes() {
        return Collections.unmodifiableSet(new HashSet<>(prefixes));
    }

    public void addPrefix(String prefix) {
        prefixes.add(prefix);
        executor.execute(() -> databaseController.addPrefix(prefix));
    }

    public void removePrefix(String prefix) {
        prefixes.remove(prefix);
        executor.execute(() -> databaseController.removePrefix(prefix));
    }

    public void clearPrefixes() {
        prefixes.clear();
        executor.execute(() -> databaseController.clearPrefixes());
    }

    public Set<String> getHumanAutoRoles() {
        return Collections.unmodifiableSet(new HashSet<>(humanAutoRoles));
    }

    public void addHumanAutoRole(String roleID) {
        humanAutoRoles.add(roleID);
        executor.execute(() -> databaseController.addHumanAutoRole(roleID));
    }

    public void removeHumanAutoRole(String roleID) {
        humanAutoRoles.remove(roleID);
        executor.execute(() -> databaseController.removeHumanAutoRole(roleID));
    }

    public void clearHumanAutoRoles() {
        humanAutoRoles.clear();
        executor.execute(() -> databaseController.clearHumanAutoRoles());
    }

    public Set<String> getBotAutoRoles() {
        return Collections.unmodifiableSet(new HashSet<>(botAutoRoles));
    }

    public void addBotAutoRole(String roleID) {
        botAutoRoles.add(roleID);
        executor.execute(() -> databaseController.addBotAutoRole(roleID));
    }

    public void removeBotAutoRole(String roleID) {
        botAutoRoles.remove(roleID);
        executor.execute(() -> databaseController.removeBotAutoRole(roleID));
    }

    public void clearBotAutoRoles() {
        botAutoRoles.clear();
        executor.execute(() -> databaseController.clearBotAutoRoles());
    }
}

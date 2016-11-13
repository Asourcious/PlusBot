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

    public Configuration(ISnowflake entity, Connection connection, ExecutorService executor) {
        this.executor = executor;

        try {
            this.databaseController = new DatabaseController(connection, entity);

            this.blacklist = databaseController.loadBlacklist();
            this.disabledCommands = databaseController.loadGuildDisabledCommands();
            this.prefixes = databaseController.loadPrefixes();
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
}

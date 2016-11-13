package org.asourcious.plusbot.handle.database;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.ISnowflake;
import net.dv8tion.jda.core.utils.SimpleLog;
import org.asourcious.plusbot.Constants;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

public class DatabaseController {

    public static final SimpleLog LOG = SimpleLog.getLog("Database");

    private ISnowflake entity;

    private TableHandler blacklist;
    private TableHandler disabledCommands;
    private TableHandler prefixes;
    private TableHandler humanAutoRoles;
    private TableHandler botAutoRoles;

    public DatabaseController(Connection connection, ISnowflake entity) throws SQLException {
        this.entity = entity;

        this.blacklist = new TableHandler(connection, Constants.BLACKLIST);
        this.disabledCommands = new TableHandler(connection,
                entity instanceof Guild
                        ? Constants.GUILD_DISABLED_COMMANDS
                        : Constants.CHANNEL_DISABLED_COMMANDS);
        this.prefixes = new TableHandler(connection, Constants.PREFIXES);
        this.humanAutoRoles = new TableHandler(connection, Constants.AUTOROLE_HUMAN);
        this.botAutoRoles = new TableHandler(connection, Constants.AUTOROLE_BOT);
    }

    public Set<String> loadBlacklist() {
        return blacklist.loadTable(entity.getId());
    }

    public void addUserToBlacklist(String userID) {
        blacklist.addEntry(entity.getId(), userID);
    }

    public void removeUserFromBlacklist(String userID) {
        blacklist.removeEntry(entity.getId(), userID);
    }

    public void clearBlacklist() {
        blacklist.clearEntries(entity.getId());
    }

    public Set<String> loadGuildDisabledCommands() {
        return disabledCommands.loadTable(entity.getId());
    }

    public void addDisabledCommand(String command) {
        disabledCommands.addEntry(entity.getId(), command);
    }

    public void removeDisabledCommand(String command) {
        disabledCommands.removeEntry(entity.getId(), command);
    }

    public void clearDisabledCommands() {
        disabledCommands.clearEntries(entity.getId());
    }

    public Set<String> loadPrefixes() {
        return prefixes.loadTable(entity.getId());
    }

    public void addPrefix(String prefix) {
        prefixes.addEntry(entity.getId(), prefix);
    }

    public void removePrefix(String prefix) {
        prefixes.removeEntry(entity.getId(), prefix);
    }

    public void clearPrefixes() {
        prefixes.clearEntries(entity.getId());
    }

    public Set<String> loadHumanAutoRoles() {
        return humanAutoRoles.loadTable(entity.getId());
    }

    public void addHumanAutoRole(String roleID) {
        humanAutoRoles.addEntry(entity.getId(), roleID);
    }

    public void removeHumanAutoRole(String roleID) {
        humanAutoRoles.removeEntry(entity.getId(), roleID);
    }

    public void clearHumanAutoRoles() {
        humanAutoRoles.clearEntries(entity.getId());
    }

    public Set<String> loadBotAutoRoles() {
        return botAutoRoles.loadTable(entity.getId());
    }

    public void addBotAutoRole(String roleID) {
        botAutoRoles.addEntry(entity.getId(), roleID);
    }

    public void removeBotAutoRole(String roleID) {
        botAutoRoles.removeEntry(entity.getId(), roleID);
    }

    public void clearBotAutoRoles() {
        botAutoRoles.clearEntries(entity.getId());
    }
}
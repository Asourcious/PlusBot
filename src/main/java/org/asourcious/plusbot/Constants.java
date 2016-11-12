package org.asourcious.plusbot;

public final class Constants {
    private Constants() {}

    // Information
    public static final String NAME = "PlusBot";
    public static final String VERSION = "2.0 DEV";
    public static final String OWNER_ID = "142843635057164288";

    // Responses
    public static final String READY_MESSAGE = NAME + " now online.";
    public static final String NOT_ENOUGH_PERMISSIONS = "You don't have the necessary permissions for this command";
    public static final String UNAVAILABLE_VIA_PM = "That command isn't supported in Private Channels.";

    // Tables
    public static final String BLACKLIST = "blacklists";
    public static final String GUILD_DISABLED_COMMANDS = "guild_disabled_commands";
    public static final String CHANNEL_DISABLED_COMMANDS = "channel_disabled_commands";
    public static final String PREFIXES = "prefixes";
    public static final String AUTOROLE_HUMAN = "autorole_human";
    public static final String AUTOROLE_Bot = "autorole_bot";
}

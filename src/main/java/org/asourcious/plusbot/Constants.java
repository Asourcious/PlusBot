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

    // Tables
    public static final String GUILD_PROFILES = "guild_profiles";
    public static final String BLACKLIST = "blacklists";
    public static final String GUILD_DISABLED_COMMANDS = "guild_disabled_commands";
    public static final String CHANNEL_DISABLED_COMMANDS = "channel_disabled_commands";
    public static final String PREFIXES = "prefixes";
    public static final String MUTES = "mutes";
    public static final String TAGS = "guild_tags";

    // Exit Codes
    public static final int BOT_INITIALIZATION_ERROR = 1;
    public static final int BOT_LOGIN_ERROR = 2;
    public static final int DATABASE_ERROR = 3;
}

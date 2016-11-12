package org.asourcious.plusbot.utils;

import net.dv8tion.jda.core.entities.Message;
import org.asourcious.plusbot.PlusBot;

public final class DiscordUtil {
    private DiscordUtil() {}

    public static String getPrefix(PlusBot plusBot, Message message) {
        if (message.getRawContent().startsWith(message.getJDA().getSelfUser().getAsMention()))
            return message.getJDA().getSelfUser().getAsMention();

        if (message.getGuild() == null)
            return null;

        // TODO: custom prefixes
        return null;
    }
}

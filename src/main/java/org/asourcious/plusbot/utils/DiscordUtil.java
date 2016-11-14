package org.asourcious.plusbot.utils;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;

import java.util.List;
import java.util.Set;

public final class DiscordUtil {
    private DiscordUtil() {}

    public static String getPrefix(PlusBot plusBot, Message message) {
        if (message.getRawContent().startsWith(message.getJDA().getSelfUser().getAsMention()))
            return message.getJDA().getSelfUser().getAsMention();

        if (message.getGuild() == null)
            return null;

        Set<String> prefixes = plusBot.getSettings().getConfiguration(message.getGuild()).getPrefixes();

        return prefixes.parallelStream().filter(prefix -> message.getContent().startsWith(prefix))
                .findAny()
                .orElse(null);
    }

    public static List<User> getTrimmedMentions(Message message) {
        List<User> users = message.getMentionedUsers();

        if (message.getRawContent().startsWith(message.getJDA().getSelfUser().getAsMention()))
            users.remove(0);

        return users;
    }

    public static boolean isCommand(PlusBot plusBot, Message message) {
        String prefix = getPrefix(plusBot, message);

        return prefix != null && plusBot.getCommandHandler().hasCommand(
                message.getRawContent().substring(prefix.length()).replaceAll("<(@(!|&)?|#)\\d+>", "").trim().split("\\s+")[0]
        );
    }
}

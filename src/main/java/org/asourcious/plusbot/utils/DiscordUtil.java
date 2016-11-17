package org.asourcious.plusbot.utils;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.config.DataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class DiscordUtil {
    private DiscordUtil() {}

    public static void checkForMissingAutoRoles(PlusBot plusBot, Guild guild) {
        DataSource autoBotRoles = plusBot.getSettings().getAutoBotRoles();
        DataSource autoHumanRoles = plusBot.getSettings().getAutoHumanRoles();

        autoBotRoles.get(guild.getId()).parallelStream()
                .filter(id -> guild.getRoleById(id) == null)
                .forEach(id -> autoBotRoles.remove(guild.getId(), id));
        autoHumanRoles.get(guild.getId()).parallelStream()
                .filter(id -> guild.getRoleById(id) == null)
                .forEach(id -> autoHumanRoles.remove(guild.getId(), id));
    }

    public static String getPrefix(PlusBot plusBot, Message message) {
        if (message.getRawContent().startsWith(message.getJDA().getSelfUser().getAsMention()))
            return message.getJDA().getSelfUser().getAsMention();

        if (message.getGuild() == null)
            return null;

        Set<String> prefixes = plusBot.getSettings().getPrefixes().get(message.getGuild().getId());

        return prefixes.parallelStream().filter(prefix -> message.getContent().startsWith(prefix))
                .findAny()
                .orElse(null);
    }

    public static List<User> getTrimmedMentions(Message message) {
        List<User> users = new ArrayList<>(message.getMentionedUsers());

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

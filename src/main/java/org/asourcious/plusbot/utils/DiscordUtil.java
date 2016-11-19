package org.asourcious.plusbot.utils;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.config.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class DiscordUtil {
    private DiscordUtil() {}

    public static void checkForMissingAutoRoles(Settings settings, Guild guild) {
        String autoBotRole = settings.getProfile(guild).getAutoBotRole();
        String autoHumanRole = settings.getProfile(guild).getAutoHumanRole();

        if (autoBotRole != null && guild.getRoleById(autoBotRole) == null)
            settings.getProfile(guild).removeAutoBotRole();
        if (autoHumanRole != null && guild.getRoleById(autoHumanRole) == null)
            settings.getProfile(guild).removeAutoHumanRole();
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

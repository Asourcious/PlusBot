package org.asourcious.plusbot.util;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang3.StringUtils;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.config.GuildProfile;
import org.asourcious.plusbot.config.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class DiscordUtils {
    private DiscordUtils() {}

    public static void checkForMissingAutoRoles(Settings settings, Guild guild) {
        String autoBotRole = settings.getProfile(guild).getProperty(GuildProfile.BOT_ROLE);
        String autoHumanRole = settings.getProfile(guild).getProperty(GuildProfile.HUMAN_ROLE);

        if (autoBotRole != null && guild.getRoleById(autoBotRole) == null)
            settings.getProfile(guild).removeProperty(GuildProfile.BOT_ROLE);
        if (autoHumanRole != null && guild.getRoleById(autoHumanRole) == null)
            settings.getProfile(guild).removeProperty(GuildProfile.HUMAN_ROLE);
    }

    public static String getPrefix(PlusBot plusBot, Message message) {
        String mention = message.getJDA().getSelfUser().getAsMention();
        String nicknameMention = "<@!" + message.getJDA().getSelfUser().getId() + ">";

        if (message.getRawContent().startsWith(mention))
            return mention;
        if (message.getRawContent().startsWith(nicknameMention))
            return nicknameMention;

        Set<String> prefixes = plusBot.getSettings().getPrefixes().get(message.getGuild().getId());

        return prefixes.parallelStream().filter(prefix -> message.getContent().startsWith(prefix))
                .findAny()
                .orElse(null);
    }

    public static String getStripped(PlusBot plusBot, Message message) {
        String prefix = getPrefix(plusBot, message);

        if (prefix == null)
            return null;

        return message.getRawContent()
                .substring(prefix.length())
                .replaceAll("<(@(!|&)?|#)\\d+>", "")
                .trim();
    }

    public static List<User> getTrimmedMentions(Message message) {
        List<User> users = new ArrayList<>(message.getMentionedUsers());

        int count = StringUtils.countMatches(message.getRawContent(), message.getJDA().getSelfUser().getAsMention());

        if (message.getRawContent().startsWith(message.getJDA().getSelfUser().getAsMention()) && count <= 1)
            users.remove(0);

        return users;
    }

    public static boolean isCommand(PlusBot plusBot, Message message) {
        String stripped = getStripped(plusBot, message);
        return stripped != null && plusBot.getCommandHandler().hasCommand(stripped.split("\\s+")[0]);
    }
}

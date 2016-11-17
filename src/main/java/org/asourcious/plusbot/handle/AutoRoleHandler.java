package org.asourcious.plusbot.handle;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.config.Settings;
import org.asourcious.plusbot.utils.DiscordUtil;

import java.util.Set;
import java.util.stream.Collectors;

public class AutoRoleHandler {

    private Settings settings;

    public AutoRoleHandler(PlusBot plusBot) {
        settings = plusBot.getSettings();
    }

    public void handleMemberJoin(Guild guild, Member member) {
        DiscordUtil.checkForMissingAutoRoles(settings, guild);

        Set<Role> roles;
        if (member.getUser().isBot()) {
            roles = settings.getAutoBotRoles().get(guild.getId()).parallelStream().map(guild::getRoleById).collect(Collectors.toSet());
        } else {
            roles = settings.getAutoHumanRoles().get(guild.getId()).parallelStream().map(guild::getRoleById).collect(Collectors.toSet());
        }

        if (!roles.isEmpty() && guild.getSelfMember().canInteract(member))
            guild.getController().addRolesToMember(member, roles).queue();
    }
}

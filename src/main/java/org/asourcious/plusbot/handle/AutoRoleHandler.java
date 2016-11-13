package org.asourcious.plusbot.handle;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.config.Settings;

import java.util.HashSet;
import java.util.Set;

public class AutoRoleHandler {

    private Settings settings;

    public AutoRoleHandler(PlusBot plusBot) {
        settings = plusBot.getSettings();
    }

    public void handleMemberJoin(Guild guild, Member member) {
        Set<Role> roles = new HashSet<>();
        if (member.getUser().isBot()) {
            Set<String> roleIDs = settings.getConfiguration(guild).getBotAutoRoles();
            roleIDs.forEach(id -> {
                if (guild.getRoleById(id) == null) {
                    settings.getConfiguration(guild).removeBotAutoRole(id);
                } else {
                    roles.add(guild.getRoleById(id));
                }
            });
        } else {
            Set<String> roleIDs = settings.getConfiguration(guild).getHumanAutoRoles();
            roleIDs.forEach(id -> {
                if (guild.getRoleById(id) == null) {
                    settings.getConfiguration(guild).removeHumanAutoRole(id);
                } else {
                    roles.add(guild.getRoleById(id));
                }
            });
        }
        if (!roles.isEmpty() && guild.getSelfMember().canInteract(member))
            guild.getController().addRolesToMember(member, roles).queue();
    }
}

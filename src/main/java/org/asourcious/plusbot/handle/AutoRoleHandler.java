package org.asourcious.plusbot.handle;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.config.GuildProfile;
import org.asourcious.plusbot.config.Settings;
import org.asourcious.plusbot.util.DiscordUtils;

public class AutoRoleHandler {

    private Settings settings;

    public AutoRoleHandler(PlusBot plusBot) {
        settings = plusBot.getSettings();
    }

    public void handleMemberJoin(Guild guild, Member member) {
        DiscordUtils.checkForMissingAutoRoles(settings, guild);

        String id = settings.getProfile(guild).getProperty(member.getUser().isBot() ? GuildProfile.BOT_ROLE : GuildProfile.HUMAN_ROLE);
        if (id == null)
            return;

        Member self = guild.getSelfMember();
        if (self.hasPermission(Permission.MANAGE_ROLES) && self.canInteract(guild.getRoleById(id)))
            guild.getController().addRolesToMember(member, guild.getRoleById(id)).queue();
    }
}

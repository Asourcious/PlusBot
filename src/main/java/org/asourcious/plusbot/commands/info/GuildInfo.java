package org.asourcious.plusbot.commands.info;

import net.dv8tion.jda.core.entities.*;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.NoArgumentCommand;
import org.asourcious.plusbot.utils.FormatUtil;

import java.util.List;
import java.util.stream.Collectors;

public class GuildInfo extends NoArgumentCommand {

    public GuildInfo(PlusBot plusBot) {
        super(plusBot);
        this.name = "GuildInfo";
        this.help = "Gives information about the server";
        this.aliases = new String[] { "ServerInfo" };
    }

    @Override
    public void execute(String stripped, Message message, User author, MessageChannel channel, Guild guild) {
        List<String> roles = guild.getRoles().parallelStream()
                .filter(role -> !role.equals(guild.getPublicRole()))
                .map(Role::getName)
                .collect(Collectors.toList());

        String msg = "";
        msg += "Name: **"  + guild.getName() + "**\n";
        msg += "ID: **"    + guild.getId() + "**\n";
        msg += "Users: **" + guild.getMembers().size() + "**\n";
        msg += "Roles: **" + FormatUtil.getFormatted(roles) + "**\n";
        msg += "Owner: **" + guild.getOwner().getEffectiveName() + "**\n";
        msg += "Creation Time: **" + FormatUtil.getFormattedTime(guild.getCreationTime()) + "**\n";
        msg += "Icon: " + guild.getIconUrl();

        channel.sendMessage(msg).queue();
    }
}
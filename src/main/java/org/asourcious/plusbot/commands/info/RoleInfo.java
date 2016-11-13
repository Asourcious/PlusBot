package org.asourcious.plusbot.commands.info;

import net.dv8tion.jda.core.entities.*;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.utils.FormatUtil;

public class RoleInfo extends Command {
    public RoleInfo(PlusBot plusBot) {
        super(plusBot);
        this.name = "RoleInfo";
        this.help = "Gives information about the specified role";
    }

    @Override
    public String isValid(Message message, String stripped) {
        if (message.getMentionedRoles().isEmpty() && stripped.isEmpty())
            return "You must either mention a role or provide a role name!";
        if (!message.getMentionedRoles().isEmpty() && !stripped.isEmpty() || message.getMentionedRoles().size() > 1)
            return "You can only specify one role!";

        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        if (guild.getRolesByName(stripped, true).size() > 1 && message.getMentionedRoles().isEmpty()) {
            channel.sendMessage("Multiple roles exist with that name! Please mention the role instead.").queue();
            return;
        }

        Role target = message.getMentionedRoles().isEmpty()
                ? guild.getRolesByName(stripped, true).get(0)
                : message.getMentionedRoles().get(0);

        String msg = "";
        msg += "Name: **" + target.getName() + "**\n";
        msg += "ID: **" + target.getId() + "**\n";
        msg += "Permissions: **" + target.getPermissions().toString() + "**\n";
        msg += "Position: **" + target.getPosition() + "**\n";
        msg += "Creation Time: **" + FormatUtil.getFormattedTime(target.getCreationTime()) + "**";

        channel.sendMessage(msg).queue();
    }
}
package org.asourcious.plusbot.commands.info;

import net.dv8tion.jda.core.entities.*;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.utils.DiscordUtil;
import org.asourcious.plusbot.utils.FormatUtil;

import java.util.List;

public class UserInfo extends Command {
    public UserInfo(PlusBot plusBot) {
        super(plusBot);
        this.name = "UserInfo";
        this.help = "Gives information about you, a mentioned user, or the user whose id you provide (As long as they are in this server)";
    }

    @Override
    public String isValid(Message message, String stripped) {
        if (DiscordUtil.getTrimmedMentions(message).size() > 1)
            return "You can only mention up to one user!";
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        List<User> users = DiscordUtil.getTrimmedMentions(message);

        Member target;
        if (users.isEmpty() && stripped.length() == 0) {
            target = guild.getMember(author);
        } else if (users.isEmpty()) {
            target = guild.getMemberById(stripped);
            if (target == null) {
                channel.sendMessage("No member with id \"" + stripped + "\" exists!").queue();
                return;
            }
        } else {
            target = guild.getMember(users.get(0));
        }

        String msg = "";
        msg += "Name: **" + target.getEffectiveName() + "**\n";
        msg += "ID: **" + target.getUser().getId() + "**\n";
        msg += "Status: **" + target.getOnlineStatus().toString() + "**\n";
        msg += "Game: **" + (target.getGame() != null ? target.getGame() : "None") + "**\n";
        msg += "Account Creation Time: **" + FormatUtil.getFormattedTime(target.getUser().getCreationTime()) + "**\n";
        msg += "Avatar: " + target.getUser().getAvatarUrl() + "\n";

        channel.sendMessage(msg).queue();
    }
}

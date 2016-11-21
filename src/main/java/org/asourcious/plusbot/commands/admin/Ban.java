package org.asourcious.plusbot.commands.admin;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.DiscordUtil;

public class Ban extends Command {
    public Ban(PlusBot plusBot) {
        super(plusBot);
        this.help = "Bans the mentioned user from the server.";
        this.requiredPermission = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public String isValid(Message message, String stripped) {
        if (message.getMentionedUsers().size() > 1 || message.getMentionedUsers().size() < 1)
            return "You must mention one user!";
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        Member target = guild.getMember(DiscordUtil.getTrimmedMentions(message).get(0));
        if (!guild.getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("I don't have the permissions to do that!").queue();
            return;
        }

        if (!guild.getSelfMember().canInteract(target)) {
            channel.sendMessage("I can't ban that person, they are higher-ranked than me!").queue();
            return;
        }

        guild.getController().kick(target).queue(n -> channel.sendMessage("Successfully banned " + target.getUser().getName() + ".").queue());
    }
}

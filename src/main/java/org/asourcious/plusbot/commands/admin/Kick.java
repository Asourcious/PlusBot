package org.asourcious.plusbot.commands.admin;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.DiscordUtil;

public class Kick extends Command {

    public Kick(PlusBot plusBot) {
        super(plusBot);
        this.help = "Kicks the mentioned user from the server.";
        this.requiredPermissions = new Permission[] { Permission.KICK_MEMBERS };
        this.permissionLevel = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public String isValid(Message message, String stripped) {
        if (!stripped.isEmpty())
            return "";
        if (message.getMentionedUsers().size() > 1 || message.getMentionedUsers().size() < 1)
            return "You must mention one user!";
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        Member target = guild.getMember(DiscordUtil.getTrimmedMentions(message).get(0));

        if (!guild.getSelfMember().canInteract(target)) {
            channel.sendMessage("I can't kick that person, they are higher-ranked than me!").queue();
            return;
        }

        guild.getController().kick(target).queue(n -> channel.sendMessage("Successfully kicked " + target.getUser().getName() + ".").queue());
    }
}

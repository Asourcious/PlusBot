package org.asourcious.plusbot.commands.admin;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.util.DiscordUtils;

import java.util.List;

public class Ban extends Command {
    public Ban(PlusBot plusBot) {
        super(plusBot);
        this.help = "Bans the mentioned user from the server.";
        this.requiredPermissions = new Permission[] { Permission.BAN_MEMBERS };
        this.permissionLevel = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public String isValid(Message message, String stripped) {
        List<User> mentions = DiscordUtils.getTrimmedMentions(message);
        if (mentions.size() > 1 || mentions.size() < 1)
            return "You must mention one user!";
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        Member target = guild.getMember(DiscordUtils.getTrimmedMentions(message).get(0));

        if (!guild.getSelfMember().canInteract(target)) {
            channel.sendMessage("I can't ban that person, they are higher-ranked than me!").queue();
            return;
        }

        guild.getController().kick(target).queue(n -> channel.sendMessage("Successfully banned " + target.getUser().getName() + ".").queue());
    }
}

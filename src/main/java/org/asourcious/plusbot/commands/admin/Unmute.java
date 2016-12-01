package org.asourcious.plusbot.commands.admin;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.managers.GuildController;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.util.DiscordUtils;

import java.util.List;

public class Unmute extends Command {

    public Unmute(PlusBot plusBot) {
        super(plusBot);
        this.help = "";
        this.permissionLevel = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public String isValid(Message message, String stripped) {
        if (!stripped.isEmpty())
            return "Only users are accepted as arguments!";
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        List<Role> roles = guild.getRolesByName("Muted", true);
        List<User> mentions = DiscordUtils.getTrimmedMentions(message);

        if (roles.isEmpty()) {
            channel.sendMessage("No muted role found! Use Mute Setup or create a role with the name \"Muted\"!").queue();
            return;
        }
        if (roles.size() > 1) {
            channel.sendMessage("Multiple roles found with name \"Muted\". Delete some and try again!").queue();
            return;
        }

        if (mentions.isEmpty()) {
            channel.sendMessage("No users were mentioned!").queue();
            return;
        }

        GuildController controller = guild.getController();
        mentions.forEach(user -> controller.removeRolesFromMember(guild.getMember(user), roles.get(0)).queue());
        channel.sendMessage("Updated mutes").queue();
    }
}

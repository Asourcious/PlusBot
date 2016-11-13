package org.asourcious.plusbot.commands.config;

import net.dv8tion.jda.core.entities.*;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.config.Configuration;

import java.util.List;

public class AutoRole extends Command {

    public AutoRole(PlusBot plusBot) {
        super(plusBot);
        this.name = "AutoRole";
        this.help = "Modifies roles to be automatically assigned to members when they join the server";
        this.requiredPermission = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public String isValid(Message message, String stripped) {
        String[] args = stripped.toLowerCase().split("\\s+", 3);
        boolean isBot = args[0].equals("bot");

        if (args.length < 1)
            return "You must use at least one argument!";
        if (isBot && args.length < 2)
            return "You must supply at least two arguments if the first argument is bot!";
        if (!args[0].equals("add") && !args[0].equals("remove") && !isBot)
            return "The first argument must be add, remove, or bot!";
        if (isBot && (!args[1].equals("add") && !args[1].equals("remove")))
            return "If the first argument is bot, the second argument must be either add or remove!";

        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, MessageChannel channel, Guild guild) {
        String[] args = stripped.toLowerCase().split("\\s+", 3);
        boolean isBot = args[0].equals("bot");
        boolean isAdd = isBot ? args[1].equals("add") : args[0].equals("add");

        Role target;
        int roleIndex = isBot ? 2 : 1;
        if (args.length == roleIndex  + 1) {
            List<Role> roles = guild.getRolesByName(args[2], true);
            if (roles.size() > 1) {
                channel.sendMessage("Multiple roles found with that name. mention the role instead!").queue();
                return;
            }
            if (roles.isEmpty()) {
                channel.sendMessage("No roles found with name \"" + args[2] + "\"").queue();
                return;
            }
            target = roles.get(0);
        } else {
            List<Role> roles = message.getMentionedRoles();
            if (roles.size() > 1) {
                channel.sendMessage("Multiple roles mentioned. Only one role can be modified at a time!").queue();
                return;
            }
            if (roles.isEmpty()) {
                channel.sendMessage("No roles mentioned!").queue();
                return;
            }
            target = roles.get(0);
        }

        Configuration configuration = settings.getConfiguration(guild);
        if (isBot) {
            if (isAdd) {
                configuration.addBotAutoRole(target.getId());
            } else {
                configuration.removeBotAutoRole(target.getId());
            }
        } else {
            if (isAdd) {
                configuration.addHumanAutoRole(target.getId());
            } else {
                configuration.removeHumanAutoRole(target.getId());
            }
        }
        channel.sendMessage("Updated auto roles").queue();
    }
}

package org.asourcious.plusbot.commands.config;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.config.Configuration;

import java.util.Set;

public class Prefix extends Command {

    public Prefix(PlusBot plusBot) {
        super(plusBot);
        this.name = "Prefix";
        this.help = "Modifies custom prefixes for the server.";
        this.requiredPermission = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public String isValid(Message message, String stripped) {
        String[] args = stripped.split("\\s+", 2);

        if (args.length == 0)
            return "You must supply arguments!";

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("clear"))
                return null;
            return "If only one argument is supplied, it must be either `clear` or `list`.";
        }

        if (!args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("remove"))
            return "If two args are supplied you must supply add or remove as your first argument!";
        if (args[1].length() > 15)
            return "The maximum supported prefix length is 15.";

        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        String[] args = stripped.split("\\s+", 2);

        Configuration configuration = settings.getConfiguration(guild);
        if (args[0].equalsIgnoreCase("add")) {
            if (configuration.getPrefixes().contains(args[1])) {
                channel.sendMessage("That prefix is already added!").queue();
                return;
            }

            if (configuration.getPrefixes().size() >= 15) {
                channel.sendMessage("This server already has the maximum number of prefixes, delete some to add more.").queue();
                return;
            }

            configuration.addPrefix(args[1]);
            channel.sendMessage("Added prefix **" + args[1] + "**").queue();
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (!configuration.getPrefixes().contains(args[1])) {
                channel.sendMessage("That prefix doesn't exist!").queue();
                return;
            }

            configuration.removePrefix(args[1]);
            channel.sendMessage("Removed prefix **" + args[1] + "**").queue();
        } else if (args[0].equalsIgnoreCase("clear")) {
            configuration.clearPrefixes();
            channel.sendMessage("Cleared prefixes").queue();
        } else {
            Set<String> prefixes = configuration.getPrefixes();

            String msg = "Current prefixes:\n```diff\n";
            for (String prefix : prefixes) {
                msg += "- " + prefix;
            }
            msg += "```";
            channel.sendMessage(msg).queue();
        }
    }
}

package org.asourcious.plusbot.commands.config;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandContainer;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.commands.SubCommand;

public class Prefix extends CommandContainer {

    public Prefix(PlusBot plusBot) {
        super(plusBot);
        this.name = "Prefix";
        this.help = "Modifies custom prefixes for the server.";
        this.children = new Command[] {
                new Add(plusBot),
                new Remove(plusBot),
                new Clear(plusBot),
                new List(plusBot)
        };
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

    private class Add extends SubCommand {
        public Add(PlusBot plusBot) {
            super(plusBot);
            this.name = "Add";
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            if (settings.getPrefixes().has(guild.getId(), stripped)) {
                channel.sendMessage("That prefix is already added!").queue();
                return;
            }

            if (settings.getPrefixes().get(guild.getId()).size() >= 15) {
                channel.sendMessage("This server already has the maximum number of prefixes, delete some to add more.").queue();
                return;
            }

            settings.getPrefixes().add(guild.getId(), stripped);
            channel.sendMessage("Added prefix **" + stripped + "**").queue();
        }
    }

    private class Remove extends SubCommand {
        public Remove(PlusBot plusBot) {
            super(plusBot);
            this.name = "Remove";
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            if (!settings.getPrefixes().has(guild.getId(), stripped)) {
                channel.sendMessage("That prefix doesn't exist!").queue();
                return;
            }

            settings.getPrefixes().remove(guild.getId(), stripped);
            channel.sendMessage("Removed prefix **" + stripped + "**").queue();
        }
    }

    private class Clear extends SubCommand {
        public Clear(PlusBot plusBot) {
            super(plusBot);
            this.name = "Clear";
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            settings.getPrefixes().clear(guild.getId());
            channel.sendMessage("Cleared prefixes").queue();
        }
    }

    private class List extends SubCommand {
        public List(PlusBot plusBot) {
            super(plusBot);
            this.name = "List";
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            String msg = "Current prefixes:\n```diff\n";
            for (String prefix : settings.getPrefixes().get(guild.getId())) {
                msg += "- " + prefix;
            }
            msg += "```";
            channel.sendMessage(msg).queue();
        }
    }
}

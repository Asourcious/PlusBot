package org.asourcious.plusbot.commands.config;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandContainer;
import org.asourcious.plusbot.commands.NoArgumentCommand;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Prefix extends CommandContainer {

    public Prefix(PlusBot plusBot) {
        super(plusBot);
        this.help = "Modifies custom prefixes for the server.";
        this.children = new Command[] {
                new Add(plusBot),
                new Remove(plusBot),
                new Clear(plusBot),
                new List(plusBot)
        };
        this.permissionLevel = PermissionLevel.SERVER_MODERATOR;
    }

    private class Add extends Command {
        Add(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public String isValid(Message message, String stripped) {
            if (stripped.isEmpty())
                return "You must supply at least one character!";
            if (stripped.length() > 15)
                return "The maximum supported prefix length is 15.";
            return null;
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

    private class Remove extends Command {
        Remove(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public String isValid(Message message, String stripped) {
            if (stripped.isEmpty())
                return "You must supply at least one character!";
            if (stripped.length() > 15)
                return "The maximum supported prefix length is 15.";
            return null;
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

    private class Clear extends NoArgumentCommand {
        Clear(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            settings.getPrefixes().clear(guild.getId());
            channel.sendMessage("Cleared prefixes").queue();
        }
    }

    private class List extends NoArgumentCommand {
        List(PlusBot plusBot) {
            super(plusBot);
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

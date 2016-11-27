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
import org.asourcious.plusbot.config.GuildProfile;

import java.util.List;

public class Welcome extends CommandContainer {

    public Welcome(PlusBot plusBot) {
        super(plusBot);
        this.help = "Sets the message and channel for the server's welcome message.";
        this.children = new Command[] {
                new Clear(plusBot),
                new Channel(plusBot),
                new Text(plusBot)
        };
        this.requiredPermission = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public String isValid(Message message, String stripped) {
        String[] args = stripped.toLowerCase().split("\\s+", 2);
        if ((args[0].equals("clear") || args[0].equals("channel")) && !args[2].isEmpty())
            return args[0] + " doesn't take any arguments!";
        if (args[0].equals("text") && stripped.isEmpty())
            return "You must supply a message with Text!";
        if (args[0].equals("clear") && !args[0].equals("channel") && !args[0].equals("text"))
            return "The only accepted arguments are clear, channel, and text!";

        return null;
    }

    private class Clear extends SubCommand {
        Clear(PlusBot plusBot) {
            super(plusBot);
            this.name = "Clear";
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            settings.getProfile(guild).removeProperty(GuildProfile.WELCOME_CHANNEL);
            settings.getProfile(guild).removeProperty(GuildProfile.WELCOME_MESSAGE);
            channel.sendMessage("Successfully cleared welcome").queue();
        }
    }

    private class Channel extends SubCommand {
        Channel(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            List<TextChannel> mentioned = message.getMentionedChannels();
            if (mentioned.size() != 1) {
                channel.sendMessage("You must mention one channel!").queue();
                return;
            }

            settings.getProfile(guild).setProperty(GuildProfile.WELCOME_CHANNEL, mentioned.get(0).getId());
            channel.sendMessage("Successfully updated welcome channel!").queue();
        }
    }

    private class Text extends SubCommand {
        Text(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            if (stripped.length() > 500) {
                channel.sendMessage("Maximum supported length is 500 characters!").queue();
                return;
            }

            settings.getProfile(guild).setProperty(GuildProfile.WELCOME_MESSAGE, stripped);
            channel.sendMessage("Updated Welcome message!").queue();
        }
    }
}

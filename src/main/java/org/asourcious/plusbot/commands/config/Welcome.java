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
        this.permissionLevel = PermissionLevel.SERVER_MODERATOR;
    }

    private class Clear extends NoArgumentCommand {
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

    private class Channel extends NoArgumentCommand {
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

    private class Text extends Command {
        Text(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public String isValid(Message message, String stripped) {
            if (stripped.isEmpty())
                return "You must supply a message!";
            if (stripped.length() > 500)
                return "Maximum supported length is 500 characters!";
            return null;
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            settings.getProfile(guild).setProperty(GuildProfile.WELCOME_MESSAGE, stripped);
            channel.sendMessage("Updated Welcome message!").queue();
        }
    }
}

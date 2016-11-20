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

import java.util.List;

public class Welcome extends CommandContainer {

    public Welcome(PlusBot plusBot) {
        super(plusBot);
        this.name = "Welcome";
        this.children = new Command[] {
                new Clear(plusBot),
                new Channel(plusBot),
                new Text(plusBot)
        };
        this.requiredPermission = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public String isValid(Message message, String stripped) {
        return null;
    }

    private class Clear extends SubCommand {
        public Clear(PlusBot plusBot) {
            super(plusBot);
            this.name = "Clear";
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            settings.getProfile(guild).removeWelcomeChannel();
            settings.getProfile(guild).removeWelcomeMessage();
            channel.sendMessage("Successfully cleared welcome").queue();
        }
    }

    private class Channel extends SubCommand {
        public Channel(PlusBot plusBot) {
            super(plusBot);
            this.name = "Channel";
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            List<TextChannel> mentioned = message.getMentionedChannels();
            if (mentioned.size() != 1) {
                channel.sendMessage("You must mention one channel!").queue();
                return;
            }

            settings.getProfile(guild).setWelcomeChannel(mentioned.get(0).getId());
            channel.sendMessage("Successfully updated welcome channel!").queue();
        }
    }

    private class Text extends SubCommand {
        public Text(PlusBot plusBot) {
            super(plusBot);
            this.name = "Text";
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            if (stripped.length() > 500) {
                channel.sendMessage("Maximum supported length is 500 characters!").queue();
                return;
            }

            settings.getProfile(guild).setWelcomeMessage(stripped);
            channel.sendMessage("Updated Welcome message!").queue();
        }
    }
}

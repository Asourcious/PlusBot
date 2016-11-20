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

public class WelcomeDM extends CommandContainer {

    public WelcomeDM(PlusBot plusBot) {
        super(plusBot);
        this.name = "WelcomeDM";
        this.children = new Command[] {
                new Clear(plusBot),
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
            settings.getProfile(guild).removeWelcomeDMMessage();
            channel.sendMessage("Cleared WelcomeDM.").queue();
        }
    }

    private class Text extends SubCommand {
        public Text(PlusBot plusBot) {
            super(plusBot);
            this.name = "Text";
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            settings.getProfile(guild).setWelcomeDMMessage(stripped);
            channel.sendMessage("Updated WelcomeDM text.").queue();
        }
    }
}

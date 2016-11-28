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

public class WelcomeDM extends CommandContainer {

    public WelcomeDM(PlusBot plusBot) {
        super(plusBot);
        this.help = "Sets the message to be DM'ed to users when they join the server";
        this.children = new Command[] {
                new Clear(plusBot),
                new Text(plusBot)
        };
        this.permissionLevel = PermissionLevel.SERVER_MODERATOR;
    }

    private class Clear extends NoArgumentCommand {
        Clear(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            settings.getProfile(guild).removeProperty(GuildProfile.WELCOME_DM_MESSAGE);
            channel.sendMessage("Cleared WelcomeDM.").queue();
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
            return null;
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            settings.getProfile(guild).setProperty(GuildProfile.WELCOME_DM_MESSAGE, stripped);
            channel.sendMessage("Updated WelcomeDM text.").queue();
        }
    }
}

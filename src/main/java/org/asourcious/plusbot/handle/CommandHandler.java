package org.asourcious.plusbot.handle;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;

public class CommandHandler {

    private PlusBot plusBot;

    public CommandHandler(PlusBot plusBot) {
        this.plusBot = plusBot;
    }

    public void handle(Message message, User author, MessageChannel channel, Guild guild, boolean isPrivate) {

    }
}

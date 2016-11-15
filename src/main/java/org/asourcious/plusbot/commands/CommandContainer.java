package org.asourcious.plusbot.commands;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;

public abstract class CommandContainer extends Command {
    public CommandContainer(PlusBot plusBot) {
        super(plusBot);
    }

    @Override
    public final void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {}
}

package org.asourcious.plusbot.commands;

import net.dv8tion.jda.core.entities.Message;
import org.asourcious.plusbot.PlusBot;

public abstract class SubCommand extends Command {
    public SubCommand(PlusBot plusBot) {
        super(plusBot);
    }

    @Override
    public final String isValid(Message message, String stripped) {
        return null;
    }
}

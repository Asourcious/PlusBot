package org.asourcious.plusbot.commands;

import net.dv8tion.jda.core.entities.Message;
import org.asourcious.plusbot.PlusBot;

public abstract class NoArgumentCommand extends Command {

    public NoArgumentCommand(PlusBot plusBot) {
        super(plusBot);
    }

    @Override
    public final String isValid(Message message, String stripped) {
        if (!stripped.isEmpty())
            return "The " + name + " command doesn't take any arguments!";

        return null;
    }
}

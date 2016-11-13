package org.asourcious.plusbot.commands.fun;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;

public class Google extends Command {

    public Google(PlusBot plusBot) {
        super(plusBot);
        this.name = "Google";
        this.help = "Searches google for the specified query.";
        this.aliases = new String[] { "g" };
    }

    @Override
    public String isValid(Message message, String stripped) {
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        String result = plusBot.getGoogleSearchHandler().search(stripped);

        if (result == null) {
            channel.sendMessage("Error searching!").queue();
            return;
        }

        channel.sendMessage(result).queue();
    }
}

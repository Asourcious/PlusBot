package org.asourcious.plusbot.commands.info;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.utils.FormatUtil;

import java.util.List;

public class Help extends Command {

    public Help(PlusBot plusBot) {
        super(plusBot);
        this.name = "Help";
        this.help = "Lists all commands, or gives information about a specific command";
        this.isPMSupported = true;
        this.aliases = new String[] { "Commands", "CommandList" };
    }

    @Override
    public String isValid(Message message, String stripped) {
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, MessageChannel channel, Guild guild) {
        String msg = "";

        if (stripped.length() == 0) {
            msg += "Currently supported commands: ";
            msg += "```diff\n";
            List<Command> commands = plusBot.getCommandHandler().getRegisteredCommands();
            for (Command command : commands) {
                msg += "- " + command.getName() + "\n";
            }
        } else {
            Command command = plusBot.getCommandHandler().getCommand(stripped);
            msg += "```diff\n";

            if (command == null) {
                channel.sendMessage("There is no command with the name \"" + stripped + "\"").queue();
                return;
            }

            msg += "- Name: " + command.getName() + "\n";
            msg += "+ Help: " + command.getHelp() + "\n";
            msg += "+ Aliases: " + FormatUtil.getFormatted(command.getAliases()) + "\n";
            msg += "+ Required Permission: " + command.getRequiredPermission().toString() + "\n";
        }
        msg += "```";

        channel.sendMessage(msg).queue();
    }
}
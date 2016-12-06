package org.asourcious.plusbot.commands.info;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.util.FormatUtils;

import java.awt.Color;

public class Help extends Command {

    public Help(PlusBot plusBot) {
        super(plusBot);
        this.help = "Lists all commands, or gives information about a specific command";
        this.aliases = new String[] { "Commands", "CommandList" };
    }

    @Override
    public String isValid(Message message, String stripped) {
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.GREEN);

        if (stripped.length() == 0) {
            embedBuilder.setAuthor("Currently supported commands", null, null);
            plusBot.getCommandHandler().getRegisteredCommands()
                    .forEach(command -> embedBuilder.addField(command.getName(), command.getHelp(), true));
        } else {
            Command command = plusBot.getCommandHandler().getCommand(stripped);

            if (command == null) {
                channel.sendMessage("There is no command with the name \"" + stripped + "\"").queue();
                return;
            }

            embedBuilder.addField("Name", command.getName(), true);
            embedBuilder.addField("Help", command.getHelp(), true);
            embedBuilder.addField("Ratelimit", command.getRateLimit() != null ? command.getRateLimit().toString() : "None", true);
            embedBuilder.addField("Aliases", FormatUtils.getFormatted(command.getAliases()), true);
            embedBuilder.addField("Required Permission", command.getPermissionLevel().toString(), true);
        }

        channel.sendMessage(embedBuilder.build()).queue();
    }
}
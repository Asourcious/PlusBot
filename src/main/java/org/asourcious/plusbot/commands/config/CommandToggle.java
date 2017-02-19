package org.asourcious.plusbot.commands.config;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandContainer;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.config.DataSource;

public class CommandToggle extends CommandContainer {

    public CommandToggle(PlusBot plusBot) {
        super(plusBot);
        this.name = "Command";
        this.help = "Used to enable and disable commands on a channel or server-wide basis";
        this.children = new Command[] {
                new Enable(plusBot),
                new Disable(plusBot)
        };
        this.permissionLevel = PermissionLevel.SERVER_MODERATOR;
    }

    private class Enable extends Command {
        Enable(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public String isValid(Message message, String stripped) {
            return checkArguments(stripped);
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            String[] args = stripped.split("\\s+");
            boolean isChannel = args.length == 2;

            String containerId = isChannel ? channel.getId() : guild.getId();
            DataSource<String> disabledCommands = isChannel ? settings.getChannelDisabledCommands() : settings.getGuildDisabledCommands();
            if (args[0].equals("all")) {
                disabledCommands.clear(containerId);
                channel.sendMessage("Enabled all commands in **" + (isChannel ? channel.getName() : guild.getName()) + "**.").queue();
            } else {
                if (!disabledCommands.has(containerId, args[0])) {
                    channel.sendMessage("That command is already enabled!").queue();
                    return;
                }
                disabledCommands.remove(containerId, args[0]);
                channel.sendMessage("Successfully enabled command.").queue();
            }
        }
    }

    private class Disable extends Command {
        Disable(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public String isValid(Message message, String stripped) {
            return checkArguments(stripped);
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            String[] args = stripped.split("\\s+");
            boolean isChannel = args.length == 2;

            String containerId = isChannel ? channel.getId() : guild.getId();
            DataSource<String> disabledCommands = isChannel ? settings.getChannelDisabledCommands() : settings.getGuildDisabledCommands();
            if (args[0].equals("all")) {
                channel.sendMessage("Disabling all commands is not allowed!").queue();
            } else {
                if (disabledCommands.has(containerId, args[0])) {
                    channel.sendMessage("That command is already disabled!").queue();
                    return;
                }
                disabledCommands.add(containerId, args[0]);
                channel.sendMessage("Successfully disabled command.").queue();
            }
        }
    }

    private String checkArguments(String stripped) {
        String[] args = stripped.toLowerCase().split("\\s+");
        if (args.length == 0 || args.length > 2)
            return "You must provide at between one and two arguments for this command";
        if (args.length == 2 && !args[1].equals("server"))
            return "The only accepted second argument is server";
        if (!args[0].equals("all") && !plusBot.getCommandHandler().hasCommand(args[0]))
            return "There is no command with the name \"" + args[1] + "\"!";

        return null;
    }
}

package org.asourcious.plusbot.commands.config;

import net.dv8tion.jda.core.entities.*;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.commands.SubCommand;
import org.asourcious.plusbot.config.Configuration;

public class CommandToggle extends Command {

    public CommandToggle(PlusBot plusBot) {
        super(plusBot);
        this.name = "Command";
        this.help = "Used to enable and disable commands on a channel or server-wide basis";
        this.children = new Command[] {
                new Enable(plusBot),
                new Disable(plusBot)
        };
        this.requiredPermission = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public String isValid(Message message, String stripped) {
        String[] args = stripped.toLowerCase().split("\\s+");
        if (args.length < 2 || args.length > 3)
            return "You must supply between 2 and 3 arguments!";
        if (!args[0].equals("enable") && !args[0].equals("disable"))
            return "The first argument mus be \"enable\" or \"disable\"!";
        if (!args[1].equals("all") && !plusBot.getCommandHandler().hasCommand(args[1]))
            return "There is no command with the name \"" + args[1] + "\"!";
        if (args.length == 3 && !args[2].equals("server"))
            return "The only valid third argument is \"server\"!";

        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {}

    private class Enable extends SubCommand {
        public Enable(PlusBot plusBot) {
            super(plusBot);
            this.name = "Enable";
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            String[] args = stripped.split("\\s+");
            boolean isChannel = args.length == 2;

            Configuration config = isChannel ? settings.getConfiguration(channel) : settings.getConfiguration(guild);
            if (args[0].equals("all")) {
                config.clearDisabledCommands();
                channel.sendMessage("Enabled all commands in **" + (isChannel ? channel.getName() : guild.getName()) + "**.").queue();
            } else {
                if (!config.getDisabledCommands().contains(args[1])) {
                    channel.sendMessage("That command is already enabled!").queue();
                    return;
                }
                config.removeDisabledCommand(args[1]);
                channel.sendMessage("Successfully enabled command.").queue();
            }
        }
    }

    private class Disable extends SubCommand {
        public Disable(PlusBot plusBot) {
            super(plusBot);
            this.name = "Disable";
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            String[] args = stripped.split("\\s+");
            boolean isChannel = args.length == 2;

            Configuration config = isChannel ? settings.getConfiguration(channel) : settings.getConfiguration(guild);
            if (args[0].equals("all")) {
                channel.sendMessage("Disabling all commands is not allowed!").queue();
            } else {
                if (config.getDisabledCommands().contains(args[1])) {
                    channel.sendMessage("That command is already disabled!").queue();
                    return;
                }
                config.addDisabledCommand(args[1]);
                channel.sendMessage("Successfully disabled command.").queue();
            }
        }
    }
}

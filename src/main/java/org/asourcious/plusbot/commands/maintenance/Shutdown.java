package org.asourcious.plusbot.commands.maintenance;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.BootLoader;
import org.asourcious.plusbot.Constants;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.NoArgumentCommand;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Shutdown extends NoArgumentCommand {

    public Shutdown(PlusBot plusBot) {
        super(plusBot);
        this.name = "Shutdown";
        this.help = "Shuts down " + Constants.NAME + ". Only available to the bot owner";
        this.isPMSupported = true;
        this.requiredPermission = PermissionLevel.OWNER;
    }

    @Override
    public void execute(String stripped, Message message, User author, MessageChannel channel, Guild guild) {
        channel.sendMessage("Shutting down...").queue();
        BootLoader.shutdown();
    }
}

package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.NoArgumentCommand;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Clear extends NoArgumentCommand {
    public Clear(PlusBot plusBot) {
        super(plusBot);
        this.help = "Clears the audio queue";
        this.permissionLevel = PermissionLevel.DJ;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        plusBot.getPlayerHandler().getPlayer(guild).setUpdateChannel(channel);
        plusBot.getPlayerHandler().getPlayer(guild).clear();
        channel.sendMessage("Cleared queue.").queue();
    }
}

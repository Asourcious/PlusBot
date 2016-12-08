package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.NoArgumentCommand;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Pause extends NoArgumentCommand {

    public Pause(PlusBot plusBot) {
        super(plusBot);
        this.help = "Pauses playback of the queue.";
        this.permissionLevel = PermissionLevel.DJ;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        plusBot.getPlayerHandler().getPlayer(guild).setUpdateChannel(channel);
        plusBot.getPlayerHandler().getPlayer(guild).pause();
        channel.sendMessage("Paused playback.").queue();
    }
}

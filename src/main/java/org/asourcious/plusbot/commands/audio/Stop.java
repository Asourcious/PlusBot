package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.NoArgumentCommand;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Stop extends NoArgumentCommand {

    public Stop(PlusBot plusBot) {
        super(plusBot);
        this.help = "Stops the audio queue.";
        this.permissionLevel = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        plusBot.getPlayerHandler().getPlayer(guild).stop();
        plusBot.getPlayerHandler().getPlayer(guild).setUpdateChannel(channel);
        channel.sendMessage("Stopped playback.").queue();
    }
}

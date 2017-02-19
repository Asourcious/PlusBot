package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.NoArgumentCommand;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.handle.audio.Player;

public class ForceSkip extends NoArgumentCommand {

    public ForceSkip(PlusBot plusBot) {
        super(plusBot);
        this.help = "Skips the current song, bypassing the vote-skip system";
        this.permissionLevel = PermissionLevel.DJ;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        Player player = plusBot.getPlayerHandler().getPlayer(guild);
        player.setUpdateChannel(channel);

        channel.sendMessage("Skipping **" + player.getPlayingTrack().getInfo().title + "**").queue();
        plusBot.getPlayerHandler().getPlayer(guild).skip();
    }
}

package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.NoArgumentCommand;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.handle.audio.Player;

public class Shuffle extends NoArgumentCommand {

    public Shuffle(PlusBot plusBot) {
        super(plusBot);
        this.help = "Toggles shuffling for the audio queue.";
        this.permissionLevel = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        Player player = plusBot.getPlayerHandler().getPlayer(guild);
        player.setUpdateChannel(channel);

        player.setShuffle(!player.isShuffle());
        channel.sendMessage("Shuffling is now " + (player.isShuffle() ? "enabled." : "disabled.")).queue();
    }
}

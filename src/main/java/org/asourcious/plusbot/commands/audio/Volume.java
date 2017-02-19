package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang3.math.NumberUtils;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.handle.audio.Player;

public class Volume extends Command {

    public Volume(PlusBot plusBot) {
        super(plusBot);
        this.help = "Updates the volume of the audio playback";
        this.permissionLevel = PermissionLevel.EVERYONE;
    }

    @Override
    public String isValid(Message message, String stripped) {
        if (stripped.isEmpty())
            return null;
        if (!NumberUtils.isParsable(stripped))
            return "That is not a valid number!";

        int volume = NumberUtils.toInt(stripped);
        if (volume < 1 || volume > 100)
            return "You must enter a value between 1 and 100";

        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        Player player = plusBot.getPlayerHandler().getPlayer(guild);
        player.setUpdateChannel(channel);

        if (stripped.isEmpty()) {
            channel.sendMessage("Current volume is **" + player.getVolume() + "**").queue();
            return;
        }

        int old = player.getVolume();
        player.setVolume(Integer.parseInt(stripped));
        channel.sendMessage("Updated volume from **" + old + "** to **" + player.getVolume() + "**").queue();
    }
}

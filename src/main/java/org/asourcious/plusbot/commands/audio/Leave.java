package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.NoArgumentCommand;
import org.asourcious.plusbot.handle.audio.Player;

public class Leave extends NoArgumentCommand {

    public Leave(PlusBot plusBot) {
        super(plusBot);
        this.help = "Leaves the current voice channel";
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        Player player = plusBot.getPlayerHandler().getPlayer(guild);

        if (!player.isConnected()) {
            return;
        }

        player.leave();
    }
}

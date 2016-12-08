package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.core.entities.*;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.handle.audio.Player;

import java.util.List;

public class Join extends Command {
    public Join(PlusBot plusBot) {
        super(plusBot);
    }

    @Override
    public String isValid(Message message, String stripped) {
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        Player player = plusBot.getPlayerHandler().getPlayer(guild);
        player.setUpdateChannel(channel);

        VoiceChannel target;
        if (stripped.isEmpty()) {
            target = guild.getMember(author).getVoiceState().getChannel();

            if (target == null) {
                return;
            }
        } else {
            List<VoiceChannel> channels = guild.getVoiceChannelsByName(stripped, true);
            if (channels.isEmpty()) {
                return;
            }
            if (channels.size() > 1) {
                return;
            }

            target = channels.get(0);

            if (target == null) {
                return;
            }
        }

        player.join(target);
    }
}

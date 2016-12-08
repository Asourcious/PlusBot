package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.handle.audio.Player;

import java.util.List;

public class Join extends Command {
    public Join(PlusBot plusBot) {
        super(plusBot);
        this.help = "Joins a voice channel with the specified name, or the one the message sender is in if no name is provided";
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
                channel.sendMessage("You aren't in a voice channel!").queue();
                return;
            }
        } else {
            List<VoiceChannel> channels = guild.getVoiceChannelsByName(stripped, true);
            if (channels.isEmpty()) {
                channel.sendMessage("There are no channels with that name!").queue();
                return;
            }
            if (channels.size() > 1) {
                channel.sendMessage("There are multiple channels with that name!").queue();
                return;
            }

            target = channels.get(0);
        }

        if (!guild.getSelfMember().hasPermission(target, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK)) {
            channel.sendMessage("I can't send audio in that channel!").queue();
            return;
        }

        player.join(target);
    }
}

package org.asourcious.plusbot.commands.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.NoArgumentCommand;

import java.awt.Color;

public class NowPlaying extends NoArgumentCommand {

    public NowPlaying(PlusBot plusBot) {
        super(plusBot);
        this.help = "Returns information about the current song.";
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        plusBot.getPlayerHandler().getPlayer(guild).setUpdateChannel(channel);
        AudioTrack track = plusBot.getPlayerHandler().getPlayer(guild).getPlayingTrack();

        if (track == null) {
            channel.sendMessage("Nothing is playing!").queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setAuthor("Now Playing:", null, null);
        embedBuilder.addField("Title", track.getInfo().title, true);
        embedBuilder.addField("Author", track.getInfo().author, true);
        embedBuilder.addField("Length", track.getInfo().length == Long.MAX_VALUE ? "Stream" : (track.getInfo().length / 1000) + " seconds", true);

        channel.sendMessage(embedBuilder.build()).queue();
    }
}

package org.asourcious.plusbot.handle.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.TextChannel;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AudioLoader implements AudioLoadResultHandler {

    private Player player;
    private TextChannel updateChannel;
    private boolean isSearch;

    public AudioLoader(Player player, TextChannel updateChannel, boolean isSearch) {
        this.player = player;
        this.updateChannel = updateChannel;
        this.isSearch = isSearch;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        updateChannel.sendMessage("Added **" + track.getInfo().title + "** to queue.").queue();
        player.addTrack(track);

        if (!player.isPaused())
            player.play();
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        if (isSearch) {
            List<AudioTrack> tracks = playlist.getTracks();
            String msg = "Search Results:\n";
            for (int i = 0; i < tracks.size(); i++) {
                msg += (i + 1) + ") **" + tracks.get(i).getInfo().title + "**\n";
            }
            msg += "Use `select x` to pick the result to add.";
            updateChannel.sendMessage(msg).queue();
            player.setSearchResults(playlist.getTracks());
            return;
        }

        updateChannel.sendMessage("Found Playlist **" + playlist.getName() + "** with **" + playlist.getTracks().size() + "** entries.").queue();
        for (AudioTrack track : playlist.getTracks()) {
            player.addTrack(track);
            if (!player.isPaused())
                player.play();
        }
        updateChannel.sendMessage("Finished queueing Playlist").queue();
    }

    @Override
    public void noMatches() {
        updateChannel.sendMessage("No sources found!").queue();
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        if (exception.severity == FriendlyException.Severity.COMMON) {
            updateChannel.sendMessage("Encountered a problem: " + exception.getMessage()).queue();
        } else {
            LoggerFactory.getLogger("Player").warn("An exception occurred", exception);
        }
    }
}

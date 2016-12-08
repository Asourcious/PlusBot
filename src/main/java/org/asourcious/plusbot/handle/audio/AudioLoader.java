package org.asourcious.plusbot.handle.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.TextChannel;

public class AudioLoader implements AudioLoadResultHandler {

    private Player player;
    private TrackScheduler trackScheduler;
    private TextChannel updateChannel;

    public AudioLoader(Player player, TrackScheduler trackScheduler, TextChannel updateChannel) {
        this.player = player;
        this.trackScheduler = trackScheduler;
        this.updateChannel = updateChannel;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        updateChannel.sendMessage("Added **" + track.getInfo().title + "** to queue.").queue();
        trackScheduler.add(track);

        if (!player.isPaused())
            player.play();
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        updateChannel.sendMessage("Found Playlist **" + playlist.getName() + "** with **" + playlist.getTracks().size() + "** entries.").queue();
        for (AudioTrack track : playlist.getTracks()) {
            trackScheduler.add(track);
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
        exception.printStackTrace();
    }
}

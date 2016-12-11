package org.asourcious.plusbot.handle.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import org.asourcious.plusbot.Constants;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Player extends AudioEventAdapter implements AudioSendHandler {

    private TextChannel updateChannel;

    private AudioPlayer player;
    private AudioPlayerManager playerManager;
    private AudioFrame lastFrame;
    private AudioManager audioManager;

    private Queue<AudioTrack> tracks;
    private AudioTrack lastTrack = null;

    private Random random;

    private boolean isRepeat = false;
    private boolean isShuffle = false;

    private Set<String> voteSkips;

    public Player(Guild guild, AudioPlayerManager playerManager) {
        this.player = playerManager.createPlayer();
        this.playerManager = playerManager;
        this.audioManager = guild.getAudioManager();
        this.updateChannel = guild.getPublicChannel();
        this.tracks = new ConcurrentLinkedQueue<>();
        this.random = new Random();
        this.voteSkips = new HashSet<>();

        player.addListener(this);
        player.setVolume(Constants.DEFAULT_VOLUME);
        audioManager.setSendingHandler(this);
    }

    public boolean isConnected() {
        return audioManager.isConnected();
    }

    public void join(VoiceChannel channel) {
        updateChannel.sendMessage("Joining **" + channel.getName()+ "**").queue();
        audioManager.openAudioConnection(channel);
    }

    public void leave() {
        updateChannel.sendMessage("Leaving **" + audioManager.getConnectedChannel().getName() + "**").queue();
        audioManager.closeAudioConnection();
    }

    public void queue(String url) {
        playerManager.loadItem(url, new AudioLoader(this, updateChannel));
    }



    public void play() {
        if (player.isPaused())
            player.setPaused(false);

        if (player.getPlayingTrack() == null) {
            play0(false);
        }
    }

    public void pause() {
        player.setPaused(true);
    }

    public void stop() {
        player.stopTrack();
    }

    public void clear() {
        tracks.clear();
        skip();
    }

    public void skip() {
        player.stopTrack();
        play0(true);
    }



    public boolean isPaused() {
        return player.isPaused();
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public void setShuffle(boolean shuffle) {
        this.isShuffle = shuffle;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean repeat) {
        this.isRepeat = repeat;
    }

    public int getVolume() {
        return player.getVolume();
    }

    public void setVolume(int volume) {
        player.setVolume(volume);
    }

    public AudioTrack getPlayingTrack() {
        return player.getPlayingTrack();
    }

    public void setUpdateChannel(TextChannel newChannel) {
        updateChannel = newChannel;
    }

    public int getNumberOfVoteSkips() {
        return voteSkips.size();
    }

    public boolean hasVoteSkipped(String userId) {
        return voteSkips.contains(userId);
    }

    public void registerVoteSkip(String userId) {
        voteSkips.add(userId);
    }

    public void clearVoteSkips() {
        voteSkips.clear();
    }

    // AudioEventAdapter methods

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        updateChannel.sendMessage("Now playing: **" + track.getInfo().title + "**").queue();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        voteSkips.clear();
        if (endReason == AudioTrackEndReason.FINISHED) {
            play0(false);
        }
    }

    // AudioSendHandler methods

    @Override
    public boolean canProvide() {
        lastFrame = player.provide();
        return lastFrame != null;
    }

    @Override
    public byte[] provide20MsAudio() {
        return lastFrame.data;
    }

    @Override
    public boolean isOpus() {
        return true;
    }

    protected void addTrack(AudioTrack track) {
        tracks.add(track);
    }

    private void play0(boolean skipped) {
        if (isRepeat && !skipped && lastTrack != null) {
            player.playTrack(lastTrack.makeClone());
            return;
        }

        if (isShuffle) {
            if (tracks.isEmpty())
                return;

            lastTrack = new ArrayList<>(tracks).get(random.nextInt(tracks.size()));
            tracks.remove(lastTrack);

            player.playTrack(lastTrack);
        } else {
            lastTrack = tracks.poll();
            player.playTrack(lastTrack);
        }
    }
}

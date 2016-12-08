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

public class Player extends AudioEventAdapter implements AudioSendHandler {

    private TextChannel updateChannel;

    private AudioPlayer player;
    private AudioPlayerManager playerManager;
    private TrackScheduler trackScheduler;
    private AudioFrame lastFrame;
    private AudioManager audioManager;

    public Player(Guild guild, AudioPlayer player, AudioPlayerManager playerManager) {
        this.updateChannel = guild.getPublicChannel();
        this.player = player;
        this.playerManager = playerManager;
        this.trackScheduler = new TrackScheduler();
        this.audioManager = guild.getAudioManager();

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
        playerManager.loadItem(url, new AudioLoader(this, trackScheduler, updateChannel));
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
        trackScheduler.clear();
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
        return trackScheduler.isShuffle();
    }

    public void setShuffle(boolean shuffle) {
        trackScheduler.setShuffle(shuffle);
    }

    public boolean isRepeat() {
        return trackScheduler.isRepeat();
    }

    public void setRepeat(boolean repeat) {
        trackScheduler.setRepeat(repeat);
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

    // AudioEventAdapter methods

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        updateChannel.sendMessage("Now playing: **" + track.getInfo().title + "**").queue();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
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

    private void play0(boolean skipped) {
        player.playTrack(trackScheduler.provideTrack(skipped));
    }
}

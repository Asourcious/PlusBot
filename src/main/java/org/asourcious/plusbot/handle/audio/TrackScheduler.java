package org.asourcious.plusbot.handle.audio;

import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;


import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TrackScheduler extends AudioEventAdapter {

    private Queue<AudioTrack> tracks;
    private AudioTrack lastTrack = null;

    private Random random;

    private boolean isRepeat = false;
    private boolean isShuffle = false;

    public TrackScheduler() {
        tracks = new ConcurrentLinkedQueue<>();
        random = new Random();
    }

    public void add(AudioTrack track) {
        tracks.add(track);
    }

    public void clear() {
        tracks.clear();
    }

    public AudioTrack provideTrack(boolean isSkipped) {
        if (isRepeat && !isSkipped && lastTrack != null)
            return lastTrack.makeClone();

        if (isShuffle) {
            if (tracks.isEmpty())
                return null;

            lastTrack = new ArrayList<>(tracks).get(random.nextInt(tracks.size()));
            tracks.remove(lastTrack);

            return lastTrack;
        } else {
            lastTrack = tracks.poll();
            return lastTrack;
        }
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public void setRepeat(boolean repeat) {
        this.isRepeat = repeat;
    }

    public void setShuffle(boolean shuffle) {
        this.isShuffle = shuffle;
    }
}

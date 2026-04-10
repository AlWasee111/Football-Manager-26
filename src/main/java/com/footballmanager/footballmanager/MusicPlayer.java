package com.footballmanager.footballmanager;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicPlayer {

    static MediaPlayer mediaPlayer;
    static List<String> playlist = new ArrayList<>();
    static int currentIndex;

    static boolean isPlay = true;

    public static void initPlaylist() {
        playlist.add("/music/BGmusic02.mp3");
        playlist.add("/music/BGmusic03.mp3");
        playlist.add("/music/BGmusic.mp3");
        Random random = new Random();
        currentIndex = random.nextInt(0,3);
    }

    public static void playMusic() {
        if (playlist.isEmpty()) {
            System.out.println("Playlist is empty!");
            return;
        }

        if (mediaPlayer == null) {
            playCurrentSong();
        } else {
            mediaPlayer.play();
        }
    }

    private static void playCurrentSong() {

        String path = MusicPlayer.class
                .getResource(playlist.get(currentIndex))
                .toExternalForm();

        Media media = new Media(path);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setVolume(0.5 * volume.musicVol);

        mediaPlayer.setOnEndOfMedia(() -> {
            nextSong();
        });

        mediaPlayer.play();
    }

    public static void nextSong() {
        if (playlist.isEmpty()) return;

        currentIndex = (currentIndex + 1) % playlist.size();
        playCurrentSong();
    }

    public static void previousSong() {
        if (playlist.isEmpty()) return;

        currentIndex = (currentIndex - 1 + playlist.size()) % playlist.size();
        playCurrentSong();
    }

    public static void refreshVolume() {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(0.5 * volume.musicVol);
        }
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public static void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }
}
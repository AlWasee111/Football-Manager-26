package com.footballmanager.footballmanager;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayer {
    static MediaPlayer mediaPlayer;
    static boolean isPlay = true;

    public static void playMusic(){
        if(mediaPlayer == null){
            String path = MusicPlayer.class.getResource("/music/BGmusic.mp3").toExternalForm();

            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.5);
        }
        mediaPlayer.play();
    }

    public static void stopMusic(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
    }

    public static void pauseMusic(){
        if(mediaPlayer != null){
            mediaPlayer.pause();
        }
    }
}

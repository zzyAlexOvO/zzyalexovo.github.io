package app.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class BgmManager {
    private MediaPlayer mediaPlayer;
    public void setup(){
        Media media = new Media(getClass().getResource("/audio/bgm.mp3").toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
    }
    public void pause(){
        mediaPlayer.pause();
    }
    public void play(){
        mediaPlayer.play();
    }
}

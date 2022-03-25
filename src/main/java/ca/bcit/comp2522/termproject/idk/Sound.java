package ca.bcit.comp2522.termproject.idk;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

import java.nio.file.Paths;


public class Sound {

    MediaPlayer mediaPlayer;

    public void gameSound() {
        String audio = "src/main/resources/assets/sounds/gamesoundintro.wav";
        Media media = new Media(Paths.get(audio).toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();

    }
}

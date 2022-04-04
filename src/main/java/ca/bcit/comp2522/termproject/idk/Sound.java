package ca.bcit.comp2522.termproject.idk;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import java.nio.file.Paths;

/**
 * Play in game sounds.
 *
 * @author Prince Chabveka
 * @version 2022
 */
public class Sound {

    MediaPlayer mediaPlayer;

    /**
     * Play game sound
     */
    public void playGameIntroSound(String filename) {
        Media media = new Media(Paths.get(filename).toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }
}

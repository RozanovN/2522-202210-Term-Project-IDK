package ca.bcit.comp2522.termproject.idk.sound;
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

    static MediaPlayer mediaPlayer;

    /**
     * Play game sound indefinitely
     */
    public static void playSound(String filename, boolean indefinitely) {
        Media media = new Media(Paths.get(filename).toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        if (indefinitely) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        }
        else {
            mediaPlayer.play();
        }

    }

    @Override
    public String toString() {
        return "Sound{}";
    }
}

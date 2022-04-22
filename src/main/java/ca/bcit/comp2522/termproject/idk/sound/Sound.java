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
    /**
     * Plays various game sounds, including gameplay and notifications.
     */
    static MediaPlayer mediaPlayer;

    /**
     * Play specified sound. Indefinitely if true, else just one.
     * @param filename sound file name
     * @param indefinitely a boolean
     */
    public static void playSound(final String filename, final boolean indefinitely) {
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

    /**
     * Add string method.
     *
     * @return a string
     */
    @Override
    public String toString() {
        return "Sound{}";
    }
}

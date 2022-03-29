package ca.bcit.comp2522.termproject.idk;


import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.time.TimerAction;
import javafx.util.Duration;


/**
 * Drives the game.
 *
 * @author Prince Chabveka
 * @author Nikolay Rozanov
 * @version 2022
 */
public class GameTimer {
    private TimerAction timerAction;


    /**
     * Game timer.
     *
     * Counts how long the user has been in the castle using seconds
     */
    protected void initGameTimer() {
        timerAction = FXGL.getGameTimer().runAtInterval(() -> System.out.println("Seconds: " + FXGL.getGameTimer().getNow()), Duration.seconds(1.0));
    }
}

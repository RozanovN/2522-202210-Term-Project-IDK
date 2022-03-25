package ca.bcit.comp2522.termproject.idk;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Drives the game.
 *
 * @author Nikolay Rozanov
 * @version 2022
 */
public class GameApp extends GameApplication{
    /**
     * Represents the native width of the screen for the game.
     */
    public final static int SCREEN_WIDTH = 1050;
    /**
     * Represents the native height of the screen for the game.
     */
    public final static int SCREEN_HEIGHT = 700;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(SCREEN_WIDTH);
        gameSettings.setHeight(SCREEN_HEIGHT);
    }

    @Override
    protected void initGame() {
        setLevelFromMap("game.tmx");
    }

    public static void main(final String[] args) {
        launch(args);
    }
}

package ca.bcit.comp2522.termproject.idk;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

public class GameApp extends GameApplication{
    public final static int SCREEN_WIDTH = 1050;
    public final static int SCREEN_HEIGHT = 700;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(SCREEN_WIDTH);
        gameSettings.setHeight(SCREEN_HEIGHT);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

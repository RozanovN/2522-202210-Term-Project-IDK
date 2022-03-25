package ca.bcit.comp2522.termproject.idk;

import static com.almasb.fxgl.dsl.FXGLForKtKt.addUINode;
import com.almasb.fxgl.achievement.Achievement;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.Position;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.EnumSet;



/**
 *
 */
public class MainMenu extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Castle adventure");
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setFullScreenAllowed(true);
        settings.setEnabledMenuItems(EnumSet.of(MenuItem.EXTRA));
        settings.getCredits().addAll(Arrays.asList(
                "Prince Chabveka",
                "Nikolay Rozanov"

        ));

        settings.getAchievements().add(new Achievement("Player 1", "description", "", 0));
        settings.getAchievements().add(new Achievement("Player 2", "description2", "", 1));
    }

    @Override
    protected void initGame() {
        FXGL.entityBuilder()
                .at(300, 300)
                .view(new Rectangle(100, 100, Color.BLUE))
                .view(new Text("Adventure"))
                .buildAndAttach();
        Sound gameSound = new Sound();
        gameSound.playGameIntroSound();

//        Game menu bar, with score.
  ProgressBar hpBar = new ProgressBar();
 hpBar.setMinValue(0);
 hpBar.setMaxValue(1000);
 hpBar.setCurrentValue(0);
 hpBar.setWidth(300);
 hpBar.setLabelVisible(true);
 hpBar.setLabelPosition(Position.RIGHT);
 hpBar.setFill(Color.GREEN);
// Node to add the bar
addUINode(hpBar);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
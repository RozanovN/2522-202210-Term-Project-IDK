package ca.bcit.comp2522.termproject.idk;

import com.almasb.fxgl.achievement.Achievement;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.ui.Position;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.EnumSet;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.addUINode;

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
    public final static int SCREEN_WIDTH = 1280;
    /**
     * Represents the native height of the screen for the game.
     */
    public final static int SCREEN_HEIGHT = 1220;
    private Entity player;

    /**
     * Constructs the Game Application.
     */
    public GameApp() {}

    /**
     * Configures this Game settings.
     *
     * @param gameSettings a GameSettings Object to configure
     */
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(SCREEN_WIDTH);
        gameSettings.setHeight(SCREEN_HEIGHT);


//        game menu
        gameSettings.setDeveloperMenuEnabled(true);
        gameSettings.setTitle("Castle adventure");
        gameSettings.setMainMenuEnabled(true);
        gameSettings.setGameMenuEnabled(true);
        gameSettings.setFullScreenAllowed(true);
        gameSettings.setEnabledMenuItems(EnumSet.of(MenuItem.EXTRA));

//        Add game developer credits
        gameSettings.getCredits().addAll(Arrays.asList(
                "Prince Chabveka",
                "Nikolay Rozanov"

        ));

//        view game achievements, will add more
        gameSettings.getAchievements().add(new Achievement("Player 1",
                "description", "", 0));
        gameSettings.getAchievements().add(new Achievement("Player 2",
                "description2", "", 1));

    }
    /**
     * Reads Player's input.
     */
    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).moveLeft();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.A, VirtualButton.LEFT);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).moveRight();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.D, VirtualButton.RIGHT);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onActionBegin() {
                player.getComponent(PlayerComponent.class).Jump();
            }
        }, KeyCode.W, VirtualButton.A);
    }

    /*
    * Creates a player.
     */
    private Entity createPlayer() {
        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.setBodyType(BodyType.DYNAMIC);
        physicsComponent.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(16, 38),
                BoundingShape.box(6, 8)));
        physicsComponent.setFixtureDef(new FixtureDef().friction(0.0f));

        return FXGL
                .entityBuilder()
                .bbox(new HitBox(new Point2D(50,25), BoundingShape.box(32, 35)))
                .at(25, 1)
                .with(physicsComponent, new CollidableComponent(true), new IrremovableComponent(), new PlayerComponent())
                .buildAndAttach();
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 360);
//        getPhysicsWorld().addCollisionHandler();
    }

    /**
     * Initializes the objects in the game.
     */
    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new GameEntitiesFactory());
        setLevelFromMap("game.tmx");
        player = createPlayer();
        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-250, -250, 250, 500);
//        viewport.bindToEntity(player, 250, 250);
        viewport.setLazy(true);

        //        Game menu bar, with score.
        ProgressBar hpBar = new ProgressBar();
        hpBar.setMinValue(0);
        hpBar.setMaxValue(100);
        hpBar.setCurrentValue(40);
        hpBar.setWidth(300);
        hpBar.setLabelVisible(true);
        hpBar.setLabelPosition(Position.LEFT);
        hpBar.setFill(Color.GREEN);
// Node to add the bar
        addUINode(hpBar);

        Sound gameSound = new Sound();


    gameSound.playGameIntroSound();

    }

    /**
     * Drives the game.
     *
     * @param args unused
     */
    public static void main(final String[] args) {
        launch(args);
    }
}

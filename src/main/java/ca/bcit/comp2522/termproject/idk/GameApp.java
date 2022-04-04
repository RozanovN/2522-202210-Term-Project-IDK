

        package ca.bcit.comp2522.termproject.idk;
//
//        import ca.bcit.comp2522.termproject.idk.component.AttackComponent;
//        import ca.bcit.comp2522.termproject.idk.component.PlayerComponent;
//        import ca.bcit.comp2522.termproject.idk.component.enemies.AbstractEnemyComponent;
//        import ca.bcit.comp2522.termproject.idk.component.enemies.WizardComponent;
        import ca.bcit.comp2522.termproject.idk.components.player.PlayerComponent;
        import ca.bcit.comp2522.termproject.idk.components.utility.AttackComponent;
        import ca.bcit.comp2522.termproject.idk.ui.GameMainMenu;
        import ca.bcit.comp2522.termproject.idk.ui.ProgressBar;
        import com.almasb.fxgl.app.MenuItem;
        import com.almasb.fxgl.app.scene.FXGLMenu;
        import com.almasb.fxgl.app.scene.SceneFactory;
        import com.almasb.fxgl.achievement.Achievement;
        import com.almasb.fxgl.app.GameApplication;
        import com.almasb.fxgl.app.GameSettings;
        import com.almasb.fxgl.app.scene.Viewport;
        import com.almasb.fxgl.dsl.FXGL;
        import com.almasb.fxgl.dsl.components.HealthIntComponent;
        import com.almasb.fxgl.entity.Entity;
        import com.almasb.fxgl.entity.components.CollidableComponent;
        import com.almasb.fxgl.entity.components.IrremovableComponent;
        import com.almasb.fxgl.input.UserAction;
        import com.almasb.fxgl.input.virtual.VirtualButton;
        import com.almasb.fxgl.physics.*;
        import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
        import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
        import com.almasb.fxgl.ui.Position;
        import javafx.geometry.Insets;
        import javafx.geometry.Point2D;
        import javafx.scene.Cursor;
        import javafx.scene.Group;
        import javafx.scene.control.Button;
        import javafx.scene.control.Label;
        import javafx.scene.control.PasswordField;
        import javafx.scene.control.TextField;
        import javafx.scene.input.KeyCode;
        import javafx.scene.input.MouseButton;
        import javafx.scene.layout.BorderPane;
        import javafx.scene.layout.VBox;
        import javafx.scene.paint.Color;
        import javafx.scene.shape.Rectangle;
        import javafx.scene.text.Font;

        import java.sql.*;
        import java.util.Arrays;
        import java.util.EnumSet;
        import java.util.Properties;

        import static com.almasb.fxgl.dsl.FXGL.*;
        import static com.almasb.fxgl.dsl.FXGLForKtKt.addUINode;

/**
 * Drives the game.
 *
 * @author Prince Chabveka
 * @author Nikolay Rozanov
 * @version 2022
 */

public class GameApp extends GameApplication{
    /**
=======
public class GameApp extends GameApplication {
        /**
>>>>>>> origin/master
     * Represents the native width of the screen for the game.
     */
    public final static int SCREEN_WIDTH = 1280;
    /**
     * Represents the native height of the screen for the game.
     */
    public final static int SCREEN_HEIGHT = 720;
    private Entity player;

    /**
     * Constructs the Game Application.
     */
    public GameApp() { }


    /**
     * Configures this Game settings.
     *
     * @param gameSettings a GameSettings Object to configure
     */
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(SCREEN_WIDTH);
        gameSettings.setHeight(SCREEN_HEIGHT);

        // game menu
        gameSettings.setDeveloperMenuEnabled(true);
        gameSettings.setTitle("Castle adventure");
        gameSettings.setMainMenuEnabled(true);
        gameSettings.setGameMenuEnabled(true);
        gameSettings.setFullScreenAllowed(true);
        gameSettings.setEnabledMenuItems(EnumSet.of(MenuItem.EXTRA));

        gameSettings.setSceneFactory(new SceneFactory() {

            @Override
            public FXGLMenu newMainMenu() {
                return new GameMainMenu();
            }
        });

        gameSettings.getCredits().addAll(Arrays.asList(
                "Prince Chabveka",
                "Nikolay Rozanov"

        ));

        // view game achievements, will add more
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

        getInput().addAction(new UserAction("Default Attack") {
            @Override
            protected void onActionBegin() {
                player.getComponent(PlayerComponent.class).frontDefaultAttack();
            }
        }, MouseButton.PRIMARY);
    }

    /*
     * Creates a player.
     */
    private Entity createPlayer() {
        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.setBodyType(BodyType.DYNAMIC);
        physicsComponent.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(4, 64),
                BoundingShape.box(6, 12)));
        physicsComponent.setFixtureDef(new FixtureDef().friction(0f));

        return FXGL
                .entityBuilder()
                .type(EntityType.PLAYER)
                .bbox(new HitBox(new Point2D(50,25), BoundingShape.box(24, 35)))
                .at(25, 1)
                .with(
                        physicsComponent, new CollidableComponent(true), new IrremovableComponent(), new PlayerComponent(),
                        new HealthIntComponent(100), new AttackComponent(15)
                )
                .zIndex(2)
                .buildAndAttach();
    }

    /**
     * Defines the physics in the game.
     * Handles the collisions.
     */
    @Override
    protected void initPhysics() {
        PhysicsWorld physicsWorld = getPhysicsWorld();
        physicsWorld.setGravity(0, 760);

        /*
         * Represents the enemyAttack.
         */
        CollisionHandler enemyAttack = new CollisionHandler(EntityType.ENEMY_ATTACK, EntityType.PLAYER) {

            @Override
            protected void onCollisionBegin(Entity attack, Entity player) {
                HealthIntComponent hp = player.getComponent(HealthIntComponent.class);
                int damage = attack.getComponent(AttackComponent.class).getDamage();
                hp.setValue(hp.getValue() - damage);

                System.out.println("Deal damage to player leaving " + hp.getValue());
                if (hp.isZero()) {
                    gameOver();
                }
            }
        };

        /*
         * Represents the playerAttack.
         */
        CollisionHandler playerAttack = new CollisionHandler(EntityType.PLAYER_ATTACK, EntityType.ENEMY) {

            @Override
            protected void onCollisionBegin(Entity attack, Entity foe) {
                HealthIntComponent hp = foe.getComponent(HealthIntComponent.class);
                int damage = attack.getComponent(AttackComponent.class).getDamage();

                hp.setValue(hp.getValue() - damage);

                System.out.println("foe has " + hp.getValue() + "hp");
                System.out.println("Deal damage" + damage);
                if (hp.isZero()) {
                    foe.removeFromWorld();
                }
            }
        };

        physicsWorld.addCollisionHandler(playerAttack);
        physicsWorld.addCollisionHandler(enemyAttack);
    }

    /**
     * Initializes the objects in the game.
     */
    @Override
    protected void initGame() {
        player = createPlayer();
        getGameScene().setCursor(Cursor.DEFAULT); // DEFAULT for testing purposes, for production use NONE
        getGameWorld().addEntityFactory(new GameEntitiesFactory());
        setLevelFromMap("game.tmx");
        // Camera settings
        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(0, -4550, 11550, 1915);
        viewport.setZoom(2);
        viewport.bindToEntity(player, 500, 250);
        viewport.setLazy(false);

        // Game menu bar, with score.
        ProgressBar hpBar = new ProgressBar();
        hpBar.setMaxValue(100);
        hpBar.setMinValue(0);

        // will need to modify current value based on
        hpBar.setCurrentValue(40);
        hpBar.setWidth(300);
        hpBar.setLabelVisible(true);
        hpBar.setLabelPosition(Position.LEFT);
        hpBar.setFill(Color.GREEN);
        hpBar.getInnerBar();
        // Node to add the bar
        addUINode(hpBar);

        // Notifications, press F for demo
        Notifications notify = new Notifications();
        notify.notification();


        final String inGameSound = "src/main/resources/assets/Sounds/epic_battle_music_1-6275.mp3";
        Sound.playSound(inGameSound, true);

    }

    /*
     * Returns the user to the main menu if player's hp gets 0 or lower.
     * Displays the game over message.
     */
    private void gameOver() {
        getDialogService().showMessageBox("You died...");
        getGameController().gotoMainMenu();
    }

    /*
     * Returns the user to the main menu if boss' hp gets 0 or lower.
     * Displays the victory message.
     */
    private void Victory() {
        getDialogService().showMessageBox("You won! Congratulations!");
        getGameController().gotoMainMenu();
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

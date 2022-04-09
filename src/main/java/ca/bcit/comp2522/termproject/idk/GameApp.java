package ca.bcit.comp2522.termproject.idk;

import ca.bcit.comp2522.termproject.idk.components.enemies.BossComponent;
import ca.bcit.comp2522.termproject.idk.components.player.PlayerComponent;
import ca.bcit.comp2522.termproject.idk.components.utility.AttackComponent;
import ca.bcit.comp2522.termproject.idk.entities.EntityType;
import ca.bcit.comp2522.termproject.idk.entities.GameEntitiesFactory;
import ca.bcit.comp2522.termproject.idk.sound.Sound;
import ca.bcit.comp2522.termproject.idk.ui.GameMainMenu;
import ca.bcit.comp2522.termproject.idk.ui.Notifications;
import ca.bcit.comp2522.termproject.idk.ui.ProgressBar;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.achievement.Achievement;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.*;
import com.almasb.fxgl.physics.box2d.dynamics.Body;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.ui.Position;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.addUINode;

/**
 * Drives the game.
 *
 * @author Prince Chabveka
 * @author Nikolay Rozanov
 * @version 2022
 */

public class GameApp extends GameApplication {
    /**
     * Represents the native width of the screen for the game.
     */
    public static final int SCREEN_WIDTH = 1280;
    /**
     * Represents the native height of the screen for the game.
     */
    public static final int SCREEN_HEIGHT = 720;
    private Entity player;
    private Entity boss;
    private int hp;
    private ProgressBar progressBar;

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
    protected void initSettings(final GameSettings gameSettings) {
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
        SpawnData spawnData = new SpawnData(8678, 846);

        return FXGL
            .entityBuilder(spawnData)
            .type(EntityType.PLAYER)
            .bbox(new HitBox(new Point2D(50,25), BoundingShape.box(24, 35)))
            .with(
                    physicsComponent, new CollidableComponent(true), new IrremovableComponent(),
                    new PlayerComponent(),
                    new HealthIntComponent(100), new AttackComponent(15,50,45)
            )
            .zIndex(2)
            .buildAndAttach();
    }

    private Entity createBoss() {
        SpawnData spawnData = new SpawnData(8878, 846);

        return FXGL
            .entityBuilder(spawnData)
            .type(EntityType.ENEMY)
            .type(EntityType.BOSS)
            .bbox(new HitBox(new Point2D(1,1), BoundingShape.box(48, 50)))
            .with(
                    new CollidableComponent(true), new IrremovableComponent(), new HealthIntComponent(150),
                    new AttackComponent(15,50,45), new BossComponent(), new StateComponent()
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
//                hp is being decremeneted
                HealthIntComponent hp = player.getComponent(HealthIntComponent.class);
                int damage = attack.getComponent(AttackComponent.class).getDamage();
                hp.setValue(hp.getValue() - damage);
                GameApp.this.hp = hp.getValue();
                GameApp.this.progressBar.setCurrentValue(GameApp.this.hp);

                System.out.println("Deal damage to player leaving " + hp.getValue());
                attack.removeFromWorld();
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
                attack.removeFromWorld();
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
        boss = createBoss();
        getGameScene().setCursor(Cursor.DEFAULT); // DEFAULT for testing purposes, for production use NONE
        getGameWorld().addEntityFactory(new GameEntitiesFactory());
        setLevelFromMap("game.tmx");
        // Camera settings
        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(0, -4550, 11550, 1915);
        viewport.setZoom(2.6);
        viewport.bindToEntity(player, 500, 250);
        viewport.setLazy(false);

//         Game menu bar, with score.
        this.progressBar = new ProgressBar();
        HealthIntComponent hp = player.getComponent(HealthIntComponent.class);
        this.progressBar.setMaxValue(100);
        this.progressBar.setMinValue(0);
//
//      current health is not being update, need a way to make hp global
        this.progressBar.setWidth(300);
        this.progressBar.setLabelVisible(true);
        this.progressBar.setLabelPosition(Position.LEFT);
        this.progressBar.setFill(Color.GREEN);
        this.progressBar.getInnerBar();
        // Node to add the bar
        addUINode(this.progressBar);

        // Notifications, press F for demo
        Notifications notify = new Notifications();
        notify.notification();


        final String inGameSound = "src/main/resources/assets/Sounds/epic_battle_music_1-6275.mp3";
        Sound.playSound(inGameSound, true);
        getWorldProperties().<Vec2>addListener("vector", (prev, now) -> System.out.println(prev + " " + now));
        set("vector", new Vec2(300, 300));

    }

    /**
     * UI for score, time etc, in implementation
     */
    @Override
    protected void initUI() {

        Text uiScore = getUIFactoryService().newText("", Color.RED, 20.0);
        uiScore.textProperty().bind(getip("score").asString());
        uiScore.translateXProperty().bind(getInput().mouseXUIProperty());
        uiScore.translateYProperty().bind(getInput().mouseYUIProperty());

        addUINode(uiScore);
    }

    @Override
    protected void onUpdate(double tpf) {
        inc("score", +1);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("testDouble", -1.5);
        vars.put("testBoolean", true);
        vars.put("vector", new Vec2(1, 1));

        vars.put("score", 0);
        vars.put("lives", 3);
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

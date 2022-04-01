package ca.bcit.comp2522.termproject.idk;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.image;
import static com.almasb.fxgl.dsl.FXGLForKtKt.runOnce;

/**
 * Represents a Component of the player's Entity.
 *
 * @author Nikolay Rozanov
 * @version 2022
 * @see Component
 */
public class PlayerComponent extends Component {
    private PhysicsComponent physicsComponent;
    final private AnimatedTexture animatedTexture;
    final private AnimationChannel idleAnimation;
    final private AnimationChannel walkingAnimation;
    final private AnimationChannel frontDefaultAttackingAnimation;
    private int moveSpeed;
    private int numberOfJumps;
    private double attackSpeed;
    private LocalTimer attackTimer;
    private boolean canAttack;

    /**
     * Constructs this Component.
     */
    public PlayerComponent() {
        Image idleImage = image("2D_SL_Knight_v1.0/Idle.png");
        Image movingImage = image("2D_SL_Knight_v1.0/Run.png");
        Image attackImage = image("2D_SL_Knight_v1.0/Attacks.png");

        idleAnimation = new AnimationChannel(idleImage, 2, 128, 64,
                Duration.seconds(1), 0, 7);
        walkingAnimation = new AnimationChannel(movingImage, 2, 128, 64,
                Duration.seconds(1), 0, 7);
        frontDefaultAttackingAnimation = new AnimationChannel(attackImage, 8, 128,
                64, Duration.seconds(1), 2, 9);
        animatedTexture = new AnimatedTexture(idleAnimation);
        moveSpeed = 150;
        attackSpeed = 1.0;
        numberOfJumps = 1;
        attackTimer = FXGL.newLocalTimer();
        canAttack = true;
        animatedTexture.loop();
    }

    /**
     * Adds animatedTexture to the entity.
     */
    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(50, 50));
        entity.getViewComponent().addChild(animatedTexture);

        physicsComponent.onGroundProperty().addListener((obs, old, isOnGround) -> {
            if (isOnGround) {
                System.out.println("onGround");
                numberOfJumps = 1;
            }
        });
    }

    /**
     * Reflects animation changes on frame update.
     *
     * @param timePerFrame double representing the time one frame takes
     */
    @Override
    public void onUpdate(final double timePerFrame) {
        if (attackTimer.elapsed(Duration.seconds(attackSpeed))) {
            canAttack = true;
        }

        if (physicsComponent.isMovingX()) {
            if (animatedTexture.getAnimationChannel() == idleAnimation) {
                animatedTexture.loopAnimationChannel(walkingAnimation);
            }

        } else {
            if (animatedTexture.getAnimationChannel() != idleAnimation && canAttack) {
                animatedTexture.loopAnimationChannel(idleAnimation);
            }
        }
    }

    /**
     * Moves the player right by 150 pixels.
     */
    public void moveRight() {
//        physicsComponent.setVelocityX(speed);
        physicsComponent.setVelocityX(moveSpeed);
        getEntity().setScaleX(1);
    }

    /**
     * Moves the player left by 150 pixels.
     */
    public void moveLeft() {
        physicsComponent.setVelocityX(-moveSpeed);
        getEntity().setScaleX(-1);
    }

    /**
     * Moves the player up if player has a positive number of jumps.
     */
    public void Jump() {
        final float jumpBoost = 2.8f;
        if (numberOfJumps == 0)
            return;
        System.out.println("jump");
        physicsComponent.setVelocityY(-moveSpeed * jumpBoost);
        numberOfJumps--;
        getEntity().setScaleY(1);
    }

    /**
     * Stops the player.
     */
    public void stop() {
        final int stop = 0;
        physicsComponent.setVelocityX(stop);
    }

    /**
     * Moves the player down by 150 pixels.
     */
    public void Descend() {
        physicsComponent.setVelocityY(-moveSpeed);
        getEntity().setScaleY(-1);
    }

    public void frontDefaultAttack() {
        if (canAttack) {
            animatedTexture.playAnimationChannel(frontDefaultAttackingAnimation);
            attackTimer.capture();
            canAttack = false;
        }
    }
}

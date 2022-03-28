package ca.bcit.comp2522.termproject.idk;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.image;

/**
 * Represents a Component of the player's Entity.
 *
 * @author Nikolay Rozanov
 * @version 2022
 * @see com.almasb.fxgl.entity.component.Component
 */
public class PlayerComponent extends Component {
    private PhysicsComponent physicsComponent;
    final private AnimatedTexture animatedTexture;
    final private AnimationChannel idleAnimation;
    final private AnimationChannel walkingAnimation;
    private final int speed;
    private int numberOfJumps;

    /**
     * Constructs this Component.
     */
    public PlayerComponent() {
        Image idleImage = image("2D_SL_Knight_v1.0/Idle.png");
        Image movingImage = image("2D_SL_Knight_v1.0/Run.png");
        idleAnimation = new AnimationChannel(idleImage, 2, 128, 64,
                Duration.seconds(1), 0, 7);
        walkingAnimation = new AnimationChannel(movingImage, 2, 128, 64,
                Duration.seconds(1), 0, 7);
        animatedTexture = new AnimatedTexture(idleAnimation);
        speed = 150;
        numberOfJumps = 1;
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
     * Reflects speed and animation changes on frame update.
     *
     * @param timePerFrame double representing the time one frame takes
     */
    @Override
    public void onUpdate(final double timePerFrame) {
        if (physicsComponent.isMovingX()) {
            if (animatedTexture.getAnimationChannel() == idleAnimation) {
                animatedTexture.loopAnimationChannel(walkingAnimation);
            }

        } else {
            if (animatedTexture.getAnimationChannel() != idleAnimation) {
                animatedTexture.loopAnimationChannel(idleAnimation);
            }
        }
    }

    /**
     * Moves the player right by 150 pixels.
     */
    public void moveRight() {
//        physicsComponent.setVelocityX(speed);
        physicsComponent.setVelocityX(speed);
        getEntity().setScaleX(1);
    }

    /**
     * Moves the player left by 150 pixels.
     */
    public void moveLeft() {
        physicsComponent.setVelocityX(-speed);
        getEntity().setScaleX(-1);
    }

    /**
     * Moves the player up by 150 pixels if player has a positive number of jumps.
     */
    public void Jump() {
        final float jumpBoost = 3.3f;
        if (numberOfJumps == 0)
            return;
        System.out.println("jump");
        physicsComponent.setVelocityY(-speed * jumpBoost);
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
        physicsComponent.setVelocityY(-speed);
        getEntity().setScaleY(-1);
    }
}

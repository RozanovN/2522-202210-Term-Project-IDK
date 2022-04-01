package ca.bcit.comp2522.termproject.idk.component.enemies;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.image;

public class WizardComponent extends Component{
    private PhysicsComponent physicsComponent;
    final private AnimatedTexture animatedTexture;
    final private AnimationChannel idleAnimation;
    final private AnimationChannel walkingAnimation;
    private int moveSpeed;
    private int attackSpeed;

    /**
     * Constructs this Component.
     */
    public WizardComponent() {
        Image idleImage = image("Evil Wizard/Sprites/Idle.png");
        Image movingImage = image("Evil Wizard/Sprites/Move.png");
        idleAnimation = new AnimationChannel(idleImage, 8, 150, 150,
                Duration.seconds(1), 0, 7);
        walkingAnimation = new AnimationChannel(movingImage, 8, 150, 150,
                Duration.seconds(1), 0, 7);
        animatedTexture = new AnimatedTexture(idleAnimation);
        this.moveSpeed = 50;
        this.attackSpeed = 50;
        animatedTexture.loop();
    }

    /**
     * Adds animatedTexture to the entity.
     */
    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(50, 50));
        entity.getViewComponent().addChild(animatedTexture);

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
}

package ca.bcit.comp2522.termproject.idk.component.enemies;

import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.image;

public class WizardComponent extends AbstractEnemyComponent {
    final private AnimationChannel idleAnimation;
    final private AnimationChannel walkingAnimation;
    private int moveSpeed;

    /**
     * Constructs this Component.
     */
    public WizardComponent() {
        super(EnemyInfo.WIZARD_DAMAGE, EnemyInfo.ALL_ENEMIES_ATTACK_SPEED);

        Image idleImage = image("Evil Wizard/Sprites/Idle.png");
        Image movingImage = image("Evil Wizard/Sprites/Move.png");
        this.moveSpeed = EnemyInfo.WIZARD_MOVE_SPEED;
        walkingAnimation = new AnimationChannel(movingImage, 8, 150, 150,
                Duration.seconds(1), 0, 7);
        idleAnimation = new AnimationChannel(idleImage, 8, 150, 150,
                Duration.seconds(1), 0, 7);
        animatedTexture = new AnimatedTexture(idleAnimation);
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

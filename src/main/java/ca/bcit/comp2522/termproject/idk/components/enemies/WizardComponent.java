package ca.bcit.comp2522.termproject.idk.components.enemies;

import ca.bcit.comp2522.termproject.idk.entities.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.image;
import static com.almasb.fxgl.dsl.FXGL.spawn;

/**
 * Represents the WizardComponent.
 *
 * @author Nikolay Rozanov
 * @version 2022
 */
public class WizardComponent extends AbstractEnemyComponent {
    private final  AnimationChannel idleAnimation;
    private final  AnimationChannel walkingAnimation;
    private final AnimationChannel meleeAttackAnimation;

    /**
     * Constructs this Component.
     */
    public WizardComponent() {
        super(EnemyInfo.ALL_ENEMIES_ATTACK_SPEED, EnemyInfo.WIZARD_MOVE_SPEED);

        Image idleImage = image("Evil Wizard/Sprites/Idle.png");
        Image movingImage = image("Evil Wizard/Sprites/Move.png");
        Image attackImage = image("Evil Wizard/Sprites/Attack.png");
        walkingAnimation = new AnimationChannel(movingImage, 8, 150, 150,
                Duration.seconds(1), 0, 7);
        idleAnimation = new AnimationChannel(idleImage, 8, 150, 150,
                Duration.seconds(1), 0, 7);
        meleeAttackAnimation = new AnimationChannel(attackImage, 8, 150, 150,
                Duration.seconds(1), 0, 7);
        animatedTexture = new AnimatedTexture(idleAnimation);

        animatedTexture.loop();
        attackRange = 100;
    }

    /**
     * Adds animatedTexture to the entity.
     */
    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(50, 50));
        entity.getViewComponent().addChild(animatedTexture);


        state = entity.getComponent(StateComponent.class);
        player = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).get(0);
    }

    /**
     * Reflects speed and animation changes on frame update.
     *
     * @param timePerFrame double representing the time one frame takes
     */
    @Override
    public void onUpdate(final double timePerFrame) {
        if (attackTimer.elapsed(Duration.seconds(attackSpeed))) {
            canAttack = true;
        } else if (attackTimer.elapsed(Duration.seconds(1))) {
            isIdle = true;
        }

        if (entity.distance(player) < 150) {
            state.changeState(attack);
        } else if (state.getCurrentState() != patrol) {
            defaultX = entity.getX();
            state.changeState(patrol);
        }

        if (entity.getComponent(PhysicsComponent.class).isMovingX()) {
            if (animatedTexture.getAnimationChannel() == idleAnimation) {
                animatedTexture.loopAnimationChannel(walkingAnimation);
            }

        } else {
            if (animatedTexture.getAnimationChannel() != idleAnimation && isIdle) {
                System.out.println("idle");
                animatedTexture.loopAnimationChannel(idleAnimation);
            }
        }
    }
    /**
     * Performs the default attack.
     */
    @Override
    public void defaultAttack() {
        if (canAttack) {
            isIdle = false;
            System.out.println("Attacking on " + this.entity.getX() + "and " + this.entity.getY());
            SpawnData spawnData = new SpawnData(this.entity.getX(), this.entity.getY());
            spawn("Attack", spawnData);
            animatedTexture.playAnimationChannel(meleeAttackAnimation);
            attackTimer.capture();
            canAttack = false;
        }
    }

    /**
     * Returns the attack range of this entity.
     *
     * @return an int representing the attack range.
     */
    @Override
    public int getAttackRange() {
        return attackRange;
    }

}

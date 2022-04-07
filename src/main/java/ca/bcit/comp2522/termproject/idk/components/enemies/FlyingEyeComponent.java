package ca.bcit.comp2522.termproject.idk.components.enemies;

import ca.bcit.comp2522.termproject.idk.entities.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
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
 * Represents the FlyingEye.
 *
 * @author Nikolay Rozanov
 * @version 2022
 */
public class FlyingEyeComponent extends AbstractEnemyComponent {
    private final AnimationChannel rangeAttackAnimation;
    Point2D direction;

    /**
     * Constructs this Component.
     */
    public FlyingEyeComponent() {
        super(EnemyInfo.ALL_ENEMIES_ATTACK_SPEED, EnemyInfo.MOTIONLESS_MOVE_SPEED);

        Image attackImage = image("Evil Wizard/Sprites/Attack.png");
        rangeAttackAnimation = new AnimationChannel(attackImage, 8, 150, 150,
                Duration.seconds(1), 0, 7);
        animatedTexture = new AnimatedTexture(rangeAttackAnimation);
        animatedTexture.loop();
        attackRange = 300;
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
        state.changeState(attack);
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
        }

        if (player.getScaleX() == 1) {
            entity.setScaleX(-1);
            direction = new Point2D(-(entity.getCenter().getX() - player.getX()), entity.getCenter().getY());
        } else {
            entity.setScaleX(1);
            direction = new Point2D(entity.getCenter().getX() - player.getX(), entity.getCenter().getY());
        }
    }

    @Override
    public void defaultAttack() {
        if (canAttack) {
            System.out.println("Attacking on " + this.entity.getX() + "and " + this.entity.getY());
            SpawnData spawnData = new SpawnData(this.entity.getX(), this.entity.getY()).put("direction", direction);
            spawn("newEyeProjectile", spawnData);
            attackTimer.capture();
            canAttack = false;
        }
    }

    @Override
    public int getAttackRange() {
        return attackRange;
    }

}


package ca.bcit.comp2522.termproject.idk.components.enemies;

import ca.bcit.comp2522.termproject.idk.entities.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.state.EntityState;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

/**
 * Represents the boss component.
 *
 * @author Nikolay Rozanov
 * @version 2022
 */
public final class BossComponent extends AbstractEnemyComponent {
    private final AnimationChannel idleAnimation;
    private final AnimationChannel projectileAttackAnimation;
    private final AnimationChannel invulnerabilityAnimation;
    private final AnimationChannel meleeAttackAnimation;
    private final Point2D defaultCoordinates = new Point2D(8878, 846);

    /**
     * Constructs BossComponent.
     */
    public BossComponent() {
        super(EnemyInfo.ALL_ENEMIES_ATTACK_SPEED, EnemyInfo.BOSS_MOVE_SPEED);

        Image idleImage = image("Mecha-stone Golem 0.1/PNG sheet/idle.png");
        Image projectileAttackImage = image("Mecha-stone Golem 0.1/PNG sheet/ProjectileAttack.png");
        Image invulnerabilityImage = image("Mecha-stone Golem 0.1/PNG sheet/invulnerability.png");
        Image meleeAttackImage = image("Mecha-stone Golem 0.1/PNG sheet/meleeAttack.png");
        idleAnimation = new AnimationChannel(idleImage, 7, 49, 46,
                Duration.seconds(1), 0, 6);
        projectileAttackAnimation = new AnimationChannel(projectileAttackImage, 9,
                94, 46, Duration.seconds(1), 0, 8);
        invulnerabilityAnimation = new AnimationChannel(invulnerabilityImage, 8,
                92, 46, Duration.seconds(1), 0, 7);
        meleeAttackAnimation = new AnimationChannel(meleeAttackImage, 7, 94, 50,
                Duration.seconds(1), 0, 6);
        animatedTexture = new AnimatedTexture(idleAnimation);
        animatedTexture.loop();
        attackRange = 100;
    }

    /**
     * Adds animatedTexture to the entity.
     */
    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(1, 1));
        entity.getViewComponent().addChild(animatedTexture);
        state = entity.getComponent(StateComponent.class);
        player = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).get(0);
    }


    /**
     *
     * @param tpf a double
     */
    @Override
    public void onUpdate(final double tpf) {
        if (player.getX() > 8320 && player.getX() < 9400 && player.getY() > 630 && player.getY() < 1000) {
            state.changeState(battle);
        } else {
            if (!new Point2D(entity.getX(), entity.getY()).equals(defaultCoordinates)) {
                navigateToThePoint(defaultCoordinates);
            }
        }

        if (animatedTexture.getAnimationChannel() != idleAnimation && isIdle) {

            animatedTexture.loopAnimationChannel(idleAnimation);
        }
    }

    private final EntityState battle = new EntityState("battle") {
        @Override
        public void onEntering() {
            System.out.println("enter attack stage");
        }

        @Override
        protected void onUpdate(final double tpf) {

            //System.out.println(entity.distance(player));
            if (entity.distance(player) > getAttackRange()) {

            } else {
                defaultAttack();
            }

        }
    };

    private void navigateToThePoint(final Point2D point) {
        if (entity.getX() < point.getX()) {
            entity.getTransformComponent().moveRight(1);
        } else {
            entity.getTransformComponent().moveLeft(1);
        }
        if (entity.getY() < point.getY()) {
            entity.getTransformComponent().setY(entity.getY() + 1);
        } else {
            entity.getTransformComponent().setY(entity.getY() - 1);
        }
    }


    /**
     * Demonstrates boss' default attack.
     */
    @Override
    public void defaultAttack() {

    }


    /**
     * Return attack range.
     *
     * @return an int
     */
    @Override
    public int getAttackRange() {
        return 0;
    }
}

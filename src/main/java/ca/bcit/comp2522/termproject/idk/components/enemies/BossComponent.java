package ca.bcit.comp2522.termproject.idk.components.enemies;

import ca.bcit.comp2522.termproject.idk.components.utility.ProjectileInfoComponent;
import ca.bcit.comp2522.termproject.idk.entities.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.state.EntityState;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.spawn;
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
    private final LocalTimer invulTimer;
    private boolean canBecomeInvul;
    private final int invulCooldown = 8;
    private final EntityState defaultState = new EntityState("default") {
        @Override
        protected void onUpdate(final double tpf) {
            if (player.getX() > 8320 && player.getX() < 9400 && player.getY() > 630 && player.getY() < 1000) {
                state.changeState(battle);
            }
            if (!entity.getCenter().equals(defaultCoordinates)) {
                navigateToThePoint(defaultCoordinates);
            }
        }
    };

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
        projectileAttackAnimation = new AnimationChannel(projectileAttackImage, 8,
                71, 46, Duration.seconds(1), 0, 7);
        invulnerabilityAnimation = new AnimationChannel(invulnerabilityImage, 8,
                49, 46, Duration.seconds(1), 0, 7);
        meleeAttackAnimation = new AnimationChannel(meleeAttackImage, 7, 64, 50,
                Duration.seconds(1), 0, 6);
        animatedTexture = new AnimatedTexture(idleAnimation);
        animatedTexture.loop();
        attackRange = 70;
        isIdle = false;
        invulTimer = FXGL.newLocalTimer();
        canBecomeInvul = true;
        canAttack = true;
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
        state.changeState(defaultState);
    }

    @Override
    public void onUpdate(final double tpf) {
        if (attackTimer.elapsed(Duration.seconds(attackSpeed))) {
            canAttack = true;
        } else if (attackTimer.elapsed(Duration.seconds(1))) {
            isIdle = true;
        }

        if (invulTimer.elapsed(Duration.seconds(invulCooldown))) {
            canBecomeInvul = true;

        } else if (invulTimer.elapsed(Duration.seconds(1))) {
            entity.setType(EntityType.ENEMY);
        }

        if (animatedTexture.getAnimationChannel() != idleAnimation && isIdle) {
            animatedTexture.loopAnimationChannel(idleAnimation);
        }

        if (player.getX() < entity.getX()) {
            entity.setScaleX(-1);
            entity.getComponent(ProjectileInfoComponent.class)
                    .setDirection(new Point2D(-(entity.getCenter().getX() - player.getX()), 0));
        } else {
            entity.setScaleX(1);
            entity.getComponent(ProjectileInfoComponent.class)
                    .setDirection(new Point2D(entity.getCenter().getX() + player.getX(), 0));
        }
    }

    private final EntityState battle = new EntityState("battle") {
        @Override
        public void onEntering() {
            System.out.println("enter attack stage");
        }

        @Override
        protected void onUpdate(final double tpf) {
            if (!(player.getX() > 8320 && player.getX() < 9400 && player.getY() > 630 && player.getY() < 1000)) {
                state.changeState(defaultState);
            }

            if (entity.distance(player) > getAttackRange() && entity.getScaleX() == 1
                    || entity.distance(player) > getAttackRange() + 100. && entity.getScaleX() == -1) {
                if (isIdle) {
                    navigateToThePoint(player.getCenter());
                }
            } else {
                if (Math.random() > 0.5) {
                    defaultAttack();
                } else {
                    invulnerability();
                }

            }

        }
    };

    private void navigateToThePoint(final Point2D point) {
        if (entity.getX() < point.getX()) {
            entity.getTransformComponent().moveRight(1);
        } else {
            entity.getTransformComponent().moveLeft(1);
        }
        if (entity.getY() < point.getY() - 25) {
            entity.getTransformComponent().setY(entity.getY() + 1);
        } else {
            entity.getTransformComponent().setY(entity.getY() - 1);
        }
    }

    @Override
    public void defaultAttack() {
        if (canAttack) {
            isIdle = false;
            if (Math.random() > 0.5) {
                meleeAttack();
            } else {
                rangeAttack();
            }
        }
    }

    private void meleeAttack() {
        canAttack = false;
        SpawnData spawnData = new SpawnData(this.entity.getX(), this.entity.getY());
        spawn("BossAttack", spawnData);
        animatedTexture.playAnimationChannel(meleeAttackAnimation);
        attackTimer.capture();
    }

    private void rangeAttack() {
        canAttack = false;
        animatedTexture.playAnimationChannel(projectileAttackAnimation);
        SpawnData spawnData = new SpawnData(this.entity.getX(), this.entity.getY());
        spawn("BossProjectile", spawnData);
        attackTimer.capture();
    }

    private void invulnerability() {
        if (canBecomeInvul) {
            isIdle = false;
            System.out.println("ads");
            animatedTexture.playAnimationChannel(invulnerabilityAnimation);
            entity.setType(EntityType.BOSS);
            canBecomeInvul = false;
            invulTimer.capture();
        }

    }

    @Override
    public int getAttackRange() {
        return attackRange;
    }
}

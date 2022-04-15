package ca.bcit.comp2522.termproject.idk.entities;

import ca.bcit.comp2522.termproject.idk.components.enemies.FlyingEyeComponent;
import ca.bcit.comp2522.termproject.idk.components.utility.AttackComponent;
import ca.bcit.comp2522.termproject.idk.components.enemies.EnemyInfo;
import ca.bcit.comp2522.termproject.idk.components.enemies.WizardComponent;
import ca.bcit.comp2522.termproject.idk.components.utility.PotionComponent;
import ca.bcit.comp2522.termproject.idk.components.utility.ProjectileInfoComponent;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;

/**
 * Represents the class for the factory of entities.
 *
 * @author Nikolay Rozanov
 * @version 2022
 */
public class GameEntitiesFactory implements EntityFactory {

    /**
     * Builds a Platform Entity.
     *
     * @param data a spawnData representing the Platform tile
     * @return Entity representing the Platform tile
     */
    @Spawns("Platform")
    public Entity newPlatform(final SpawnData data) {
        return FXGL
                .entityBuilder(data)
                .bbox(
                        new HitBox(
                                BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .build();
    }

    /**
     * Builds a Stairs Entity.
     *
     * @param data a spawnData representing the Stairs tile
     * @return Entity representing the Stairs tile
     */
    @Spawns("Stairs")
    public Entity newStairs(final SpawnData data) {
        return FXGL
                .entityBuilder(data)
                .bbox(
                        new HitBox(
                                BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .build();
    }

    /**
     * Builds a Ceiling Entity.
     *
     * @param data a spawnData representing the Ceiling tile
     * @return Entity representing the Ceiling tile
     */
    @Spawns("Ceiling")
    public Entity newCeiling(final SpawnData data) {
        return FXGL
                .entityBuilder(data)
                .bbox(
                        new HitBox(
                                BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .build();
    }

    /**
     * Builds a Wall Entity.
     *
     * @param data a spawnData representing the Wall tile
     * @return Entity representing the Wall tile
     */
    @Spawns("Wall")
    public Entity newWall(final SpawnData data) {
        return FXGL
                .entityBuilder(data)
                .bbox(
                        new HitBox(
                                BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .build();
    }

    /**
     * Builds a Trap Entity.
     *
     * @param data a spawnData representing the Trap tile
     * @return Entity representing the Trap tile
     */
    @Spawns("Trap")
    public Entity newTrap(final SpawnData data) {
        return FXGL
                .entityBuilder(data)
                .bbox(
                        new HitBox(
                                BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent(), new CollidableComponent())
                .build();
    }

    /**
     * Builds a Fire Wizard entity.
     *
     * @param data unused
     * @return Entity representing the Fire Wizard
     */
    @Spawns("FireWizard")
    public Entity newFireWizard(final SpawnData data) {
        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.setBodyType(BodyType.DYNAMIC);
        physicsComponent.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(1, 1),
                BoundingShape.box(6, 12)));
        physicsComponent.setFixtureDef(new FixtureDef().friction(1f));

        return FXGL
                .entityBuilder()
                .type(EntityType.ENEMY)
                .bbox(new HitBox(new Point2D(50,50), BoundingShape.box(35, 45)))
                .at(25, 1)
                .with(
                    physicsComponent, new CollidableComponent(true), new StateComponent(), new WizardComponent(),
                    new HealthIntComponent(EnemyInfo.WIZARD_MAX_HP),
                    new AttackComponent(EnemyInfo.WIZARD_DAMAGE, EnemyInfo.WIZARD_ATTACK_WIDTH,
                            EnemyInfo.WIZARD_ATTACK_HEIGHT)
                )
                .build();
    }

    /**
     * Spawns an attack entity for 2 seconds.
     *
     * @param data a SpawnData that represents the position of the spawn point
     * @return Entity representing an attack
     */
    @Spawns("Attack")
    public Entity newAttack(final SpawnData data) {
        System.out.println("built a box at" + data.getX() + " and " + data.getY());
        Point2D position = new Point2D(data.getX(), data.getY());
        EntityType type;
        Entity caller = getGameWorld().getEntitiesAt(position).get(0);
        AttackComponent attackComponent = caller.getComponent(AttackComponent.class);
        Point2D spawnPoint = new Point2D(caller.getWidth() * 2, caller.getHeight());

        if (caller.getScaleX() == -1) {
            spawnPoint = new Point2D(caller.getWidth() * -1, caller.getHeight());
        }

        if (getGameWorld().getEntitiesAt(position).get(0).getType() == EntityType.PLAYER) {
            type = EntityType.PLAYER_ATTACK;
        } else {
            type = EntityType.ENEMY_ATTACK;
        }

        Entity attack =  FXGL
            .entityBuilder(data)
            .type(type)
            .bbox(new HitBox(BoundingShape.box(attackComponent.getWidth(),
                    attackComponent.getHeight())))
            .with(
                new CollidableComponent(true),
                new ExpireCleanComponent(Duration.seconds(2)),
                attackComponent,
                new StateComponent()
            )
            .build();
        attack.translate(spawnPoint);
        return attack;
    }

    /**
     * Builds a Flying Eye entity.
     *
     * @param data unused
     * @return Entity representing the Flying Eye entity
     */
    @Spawns("FlyingEye")
    public Entity newFlyingEye(final SpawnData data) {
        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.setBodyType(BodyType.DYNAMIC);
        physicsComponent.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(1, 1),
                BoundingShape.box(6, 12)));
        physicsComponent.setFixtureDef(new FixtureDef().friction(1f));
        ProjectileInfoComponent projectileInfoComponent = new ProjectileInfoComponent(EnemyInfo.FLYING_EYE_DAMAGE,
                EnemyInfo.FLYING_EYE_PROJECTILE_IMAGE);

        return FXGL
                .entityBuilder()
                .type(EntityType.ENEMY)
                // .viewWithBBox("Monster_Creatures_Fantasy(Version 1.3)/Flying eye/FlyingEye.png")
                .bbox(new HitBox(new Point2D(65, 65), BoundingShape.box(35, 30)))
                .with(
                        physicsComponent, new CollidableComponent(true), new StateComponent(), new FlyingEyeComponent(),
                        new HealthIntComponent(EnemyInfo.FLYING_EYE_MAX_HP), projectileInfoComponent
                )
                .build();
    }

    /**
     * Spawns a new projectile for 5 seconds.
     *
     * @param data a SpawnData that represents the position of the spawn point
     * @return Entity representing an eye projectile
     */
    @Spawns("Projectile")
    public Entity newProjectile(final SpawnData data) {
        ExpireCleanComponent expireCleanComponent = new ExpireCleanComponent(Duration.seconds(5));
        expireCleanComponent.pause();
        ProjectileInfoComponent projectileInfoComponent = getGameWorld().getEntitiesAt(new Point2D(data.getX(),
                        data.getY())).get(0).getComponent(ProjectileInfoComponent.class);

        return FXGL
            .entityBuilder(data)
            .at(data.getX() + 25, data.getY() + 75)
            .type(EntityType.ENEMY_ATTACK)
            .viewWithBBox(projectileInfoComponent.getProjectilePicture())
            .with(
                new CollidableComponent(true),
                new ProjectileComponent(projectileInfoComponent.getDirection(), projectileInfoComponent.getMoveSpeed()),
                expireCleanComponent,
                new AttackComponent(projectileInfoComponent.getDamage(), 0, 0)
            )
            .rotationOrigin(0, 6.5)
            .build();
    }

    /**
     * Spawns the boss' projectile for 5 seconds.
     *
     * @param data a SpawnData that represents the position of the spawn point
     * @return Entity representing an eye projectile
     */
    @Spawns("BossProjectile")
    public Entity newBossProjectile(final SpawnData data) {
        ExpireCleanComponent expireCleanComponent = new ExpireCleanComponent(Duration.seconds(5));
        expireCleanComponent.pause();
        Entity boss = getGameWorld().getEntitiesAt(new Point2D(data.getX(), data.getY())).get(0);
        ProjectileInfoComponent projectileInfoComponent = boss.getComponent(ProjectileInfoComponent.class);
        int xCorrection = 55;

        if (boss.getScaleX() == -1) {
            xCorrection = -xCorrection;
        }

        return FXGL
                .entityBuilder(data)
                .at(data.getX() + xCorrection, data.getY())
                .type(EntityType.ENEMY_ATTACK)
                .viewWithBBox(projectileInfoComponent.getProjectilePicture())
                .with(
                        new CollidableComponent(true),
                        new ProjectileComponent(projectileInfoComponent.getDirection(), projectileInfoComponent.getMoveSpeed()),
                        expireCleanComponent,
                        new AttackComponent(projectileInfoComponent.getDamage(), 0, 0)
                )
                .rotationOrigin(0, 6.5)
                .build();
    }

    /**
     * Spawns the boss' attack entity for 2 seconds.
     *
     * @param data a SpawnData that represents the position of the spawn point
     * @return Entity representing an attack
     */
    @Spawns("BossAttack")
    public Entity newBossAttack(final SpawnData data) {
        Point2D position = new Point2D(data.getX(), data.getY());
        Entity caller = getGameWorld().getEntitiesAt(position).get(0);
        AttackComponent attackComponent = caller.getComponent(AttackComponent.class);
        Point2D spawnPoint;

        if (caller.getScaleX() == 1) {
            spawnPoint = new Point2D(50, 25);
        } else {
            spawnPoint = new Point2D(-75, 0);
        }

        Entity attack =  FXGL
                .entityBuilder(data)
                .type(EntityType.ENEMY_ATTACK)
                .bbox(new HitBox(BoundingShape.box(attackComponent.getWidth(),
                        attackComponent.getHeight())))
                .with(
                        new CollidableComponent(true),
                new ExpireCleanComponent(Duration.seconds(2)),
                        attackComponent,
                        new StateComponent()
                )
                .build();
        attack.translate(spawnPoint);
        return attack;
    }

    /**
     * Spawns a new potion entity.
     *
     * @param data a SpawnData that represents the position of the spawn point
     * @return an Entity representing potion
     */
    @Spawns("Potion")
    public Entity newPotion(final SpawnData data) {
        PotionComponent potionComponent = new PotionComponent();
        Entity potion =
            FXGL
            .entityBuilder(data)
            .type(EntityType.POTION)
            .viewWithBBox(potionComponent.getImagePath())
            .with(
                new CollidableComponent(true),
                potionComponent,
                new ExpireCleanComponent(Duration.seconds(15))
            )
            .build();
//        potion.translateY(-65);
        return potion;
    }
}

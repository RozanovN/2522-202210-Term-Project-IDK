package ca.bcit.comp2522.termproject.idk.entities;

import ca.bcit.comp2522.termproject.idk.components.utility.AttackComponent;
import ca.bcit.comp2522.termproject.idk.components.enemies.EnemyInfo;
import ca.bcit.comp2522.termproject.idk.components.enemies.WizardComponent;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
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
        physicsComponent.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(4, 64),
                BoundingShape.box(6, 12)));
        physicsComponent.setFixtureDef(new FixtureDef().friction(1f));

        return FXGL
                .entityBuilder()
                .type(EntityType.ENEMY)
                .bbox(new HitBox(new Point2D(55,50), BoundingShape.box(35, 50)))
                .at(25, 1)
                .with(
                    physicsComponent, new CollidableComponent(true), new StateComponent(), new WizardComponent(),
                    new HealthIntComponent(EnemyInfo.WIZARD_MAX_HP), new AttackComponent(EnemyInfo.WIZARD_DAMAGE)
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
        if (getGameWorld().getEntitiesAt(position).get(0).getType() == EntityType.PLAYER) {
            type = EntityType.PLAYER_ATTACK;
        } else {
            type = EntityType.ENEMY_ATTACK;
        }
        return FXGL
                .entityBuilder()
                .type(type)
                .bbox(new HitBox(new Point2D(50,25), BoundingShape.box(
                        getGameWorld().getEntitiesAt(position).get(0).getWidth() * 2, 35)))
                .at(data.getX(), data.getY())
                .with(
                    new CollidableComponent(true),
                    new ExpireCleanComponent(Duration.seconds(2)),
                    new AttackComponent(getGameWorld().getEntitiesAt(position).get(0)
                            .getComponent(AttackComponent.class).getDamage()),
                    new StateComponent()
                )
                .build();
    }
}

package ca.bcit.comp2522.termproject.idk;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Represents the class for the factory of tiles.
 */
public class GameEntitiesFactory implements EntityFactory{

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
//
//    /**
//     * Demo of player for testing purpose, to be changed later.
//     *
//     * @return Entity representing player
//     */
//    @Spawns("Player")
//    public Entity newPlayer() {
//        PhysicsComponent physicsComponent = new PhysicsComponent();
//        physicsComponent.setBodyType(BodyType.DYNAMIC);
//        return FXGL
//                .entityBuilder()
//                .view(new Rectangle(25, 25, Color.BLUE))
//                .with(physicsComponent, new CollidableComponent())
//                .build();
//    }
}

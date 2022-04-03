package ca.bcit.comp2522.termproject.idk.component.enemies;

import ca.bcit.comp2522.termproject.idk.component.AttackComponent;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;

/**
 * Represents the component shared by all enemies.
 *
 * @author Nikolay Rozanov
 * @version 2022
 */
public abstract class AbstractEnemyComponent extends Component {
    protected AnimatedTexture animatedTexture;
    protected PhysicsComponent physicsComponent;
    protected final int attackSpeed;
    protected final int moveSpeed;

    /**
     * Constructs AbstractEnemyComponent.
     *
     * @param attackSpeed an int that represents the enemy's attackSpeed
     */
    public AbstractEnemyComponent(final int attackSpeed, final int moveSpeed) {
        this.attackSpeed = attackSpeed;
        this.moveSpeed = moveSpeed;
    }

    public abstract void defaultAttack();

}

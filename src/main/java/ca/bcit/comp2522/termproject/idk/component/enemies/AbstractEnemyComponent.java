package ca.bcit.comp2522.termproject.idk.component.enemies;

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
    protected final int damage;
    protected AnimatedTexture animatedTexture;
    protected PhysicsComponent physicsComponent;
    protected final int attackSpeed;

    /**
     * Constructs AbstractEnemyComponent.
     *
     * @param damage an int that represents the enemy's damage
     * @param attackSpeed an int that represents the enemy's attackSpeed
     */
    public AbstractEnemyComponent(final int damage, final int attackSpeed) {
        this.damage = damage;
        this.attackSpeed = attackSpeed;
    }

    /**
     * Returns the enemy's damage.
     *
     * @return an int representing damage
     */
    public int getDamage() {
        return damage;
    }

}

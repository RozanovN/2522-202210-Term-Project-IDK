package ca.bcit.comp2522.termproject.idk.components.enemies;

import ca.bcit.comp2522.termproject.idk.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.state.EntityState;
import com.almasb.fxgl.entity.state.StateComponent;
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
    protected boolean canAttack;
    private final int attackSpeed;
    private final int defaultMoveSpeed;
    private int moveSpeed;
    private final Entity player;
    private final StateComponent state;
    private final double defaultX;

    /**
     * Constructs AbstractEnemyComponent.
     *
     * @param attackSpeed an int that represents the enemy's attack speed
     * @param moveSpeed an int representing the enemy's move speed
     */
    public AbstractEnemyComponent(final int attackSpeed, final int moveSpeed) {
        this.attackSpeed = attackSpeed;
        this.defaultMoveSpeed = moveSpeed;
        this.player = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).get(0);
        this.state = entity.getComponent(StateComponent.class);
        this.defaultX = entity.getX();
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(final int moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    @Override
    public void onAdded() {
        state.changeState(patrol);
    }

    @Override
    public void onUpdate(final double tpf) {

    }

    protected final EntityState patrol = new EntityState("patrol") {

        @Override
        public void onEntering() {
            moveSpeed = defaultMoveSpeed;
        }

        @Override
        public void onEnteredFrom(final EntityState previousState) {
            if (previousState == attack) {
                moveSpeed = -defaultMoveSpeed;
            }
        }

        @Override
        protected void onUpdate(final double tpf) {
            if (entity.getX() - defaultX < -50) {
                entity.getComponent(PhysicsComponent.class).setVelocityX(moveSpeed);
                entity.setScaleX(1);

            } else if (entity.getX() - defaultX > 50) {
                entity.getComponent(PhysicsComponent.class).setVelocityX(moveSpeed);
                getEntity().setScaleX(-1);
            }
        }

    };

    private final EntityState attack = new EntityState("attack") {
        @Override
        public void onEntering() {
            moveSpeed = 0;
        }

        @Override
        protected void onUpdate(final double tpf) {

            if (entity.distance(player) > 20) {
                moveSpeed = defaultMoveSpeed;
                entity.getComponent(PhysicsComponent.class).setVelocityX(moveSpeed);
                getEntity().setScaleX(-1);
            } else {
                meleeAttack();
            }
        }
    };

    public abstract void meleeAttack();
}

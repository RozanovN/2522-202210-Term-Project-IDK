package ca.bcit.comp2522.termproject.idk.components.enemies;

import ca.bcit.comp2522.termproject.idk.entities.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.state.EntityState;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.time.LocalTimer;

/**
 * Represents the component shared by all enemies.
 *
 * @author Nikolay Rozanov
 * @version 2022
 */
public abstract class AbstractEnemyComponent extends Component {
    protected AnimatedTexture animatedTexture;
    protected boolean canAttack;
    protected final LocalTimer attackTimer;
    protected final int attackSpeed;
    private final int defaultMoveSpeed;
    private int moveSpeed;
    protected Entity player;
    protected StateComponent state;
    private double defaultX;
    protected boolean isIdle;

    /**
     * Constructs AbstractEnemyComponent.
     *
     * @param attackSpeed an int that represents the enemy's attack speed
     * @param moveSpeed an int representing the enemy's move speed
     */
    public AbstractEnemyComponent(final int attackSpeed, final int moveSpeed) {
        this.attackTimer = FXGL.newLocalTimer();
        this.attackSpeed = attackSpeed;
        this.defaultMoveSpeed = moveSpeed;
        this.player = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).get(0);
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
    public void onUpdate(double tpf) {

    }

    protected final EntityState patrol = new EntityState("patrol") {

        @Override
        public void onEntering() {
            moveSpeed = defaultMoveSpeed;
        }

        @Override
        protected void onUpdate(final double tpf) {
            if (entity.getX() - defaultX < -150) {
                entity.getComponent(PhysicsComponent.class).setVelocityX(moveSpeed);
                entity.setScaleX(1);

            } else if (entity.getX() - defaultX > 150) {
                entity.getComponent(PhysicsComponent.class).setVelocityX(-moveSpeed);
                getEntity().setScaleX(-1);
            }
        }

    };

    protected final EntityState attack = new EntityState("attack") {
        @Override
        public void onEntering() {
            System.out.println("enter attack stage");
        }

        @Override
        protected void onUpdate(final double tpf) {

            //System.out.println(entity.distance(player));
            if (entity.distance(player) > 100) {
                if (entity.getX() - player.getX() < 0) {
                    getEntity().setScaleX(1);
                    moveSpeed = defaultMoveSpeed;
                } else {
                    getEntity().setScaleX(-1);
                    moveSpeed = -defaultMoveSpeed;
                }
                entity.getComponent(PhysicsComponent.class).setVelocityX(moveSpeed);
            } else {
                //System.out.println("attacking");
                moveSpeed = 0;
                entity.getComponent(PhysicsComponent.class).setVelocityX(moveSpeed);
                meleeAttack();

            }

        }
    };

    public abstract void meleeAttack();
}

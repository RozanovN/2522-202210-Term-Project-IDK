package ca.bcit.comp2522.termproject.idk.components.utility;

import ca.bcit.comp2522.termproject.idk.components.enemies.EnemyInfo;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

/**
 * Represents additional Projectile info.
 *
 * @author Nikolay Rozanov
 * @version 2022
 */
public final class ProjectileInfoComponent extends Component {
    /**
     * Represents the movement speed of the projectile.
     */
    public static final int PROJECTILE_MOVE_SPEED = 150;
    private final int damage;
    private final String projectilePicture;
    private final int moveSpeed;
    private Point2D direction;

    /**
     * Constructs Projectile Info Component.
     *
     * @param damage an int representing projectile damage
     * @param projectilePicture a String representing path to the projectile image.
     */
    public ProjectileInfoComponent(final int damage, final String projectilePicture) {
        this.damage = damage;
        this.projectilePicture = projectilePicture;
        this.moveSpeed = PROJECTILE_MOVE_SPEED;
        this.direction = null;
    }

    /**
     * Returns this projectile's damage.
     *
     * @return an int represents this projectile's damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Returns the path to this projectile's image.
     *
     * @return a String path to this projectile's image.
     */
    public String getProjectilePicture() {
        return projectilePicture;
    }

    /**
     * Returns this projectile's move speed.
     *
     * @return an int representing this projectile's move speed.
     */
    public int getMoveSpeed() {
        return moveSpeed;
    }

    /**
     * Returns this projectile's direction.
     *
     * @return a Point2D representing this projectile's direction
     */
    public Point2D getDirection() {
        return direction;
    }

    /**
     * Set this projectile's direction.
     *
     * @param direction a Point2D representing new projectile's direction
     */
    public void setDirection(final Point2D direction) {
        this.direction = direction;
    }
}

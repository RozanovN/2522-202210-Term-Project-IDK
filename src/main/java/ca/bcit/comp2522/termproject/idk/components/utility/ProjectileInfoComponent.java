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

    public int getDamage() {
        return damage;
    }

    public String getProjectilePicture() {
        return projectilePicture;
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }

    public Point2D getDirection() {
        return direction;
    }

    public void setDirection(final Point2D direction) {
        this.direction = direction;
    }
}

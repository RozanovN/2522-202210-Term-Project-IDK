package ca.bcit.comp2522.termproject.idk.components.utility;

import com.almasb.fxgl.entity.component.Component;

/**
 * Represent Attack Component.
 *
 * @author Nikolay Rozanov
 * @version 2022
 */
public class AttackComponent extends Component {
    private int damage;
    private int width;
    private int height;

    /**
     * Constructs an AttackComponent.
     *
     * @param damage an int representing the attacks' damage
     * @param height an int representing the attacks' width of the attack
     * @param width an int representing the attacks' height of the attack
     */
    public AttackComponent(final int damage, final int width, final int height) {
        this.damage = damage;
        this.width = width;
        this.height = height;
    }

    /**
     * Gets this AttackComponent's width.
     *
     * @return an int representing this AttackComponent's width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets this AttackComponent's height.
     *
     * @return an int representing this AttackComponent's height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets this AttackComponent's damage.
     *
     * @return an int representing this AttackComponent's damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Sets this AttackComponent's damage.
     *
     * @param damage an int representing this AttackComponent's damage.
     */
    public void setDamage(final int damage) {
        this.damage = damage;
    }

    /**
     * Multiples this AttackComponent's damage by the given percentage
     *
     * @param percentage a double representing the percentage multiplier
     */
    public void changeDamageByPercentage(final double percentage) {
        this.damage = (int) (damage * percentage);
    }

    /**
     * Changes this AttackComponent's damage by the given value
     *
     * @param value an int representing the value
     */
    public void changeDamageByValue(final double value) {
        this.damage += value;
    }
}

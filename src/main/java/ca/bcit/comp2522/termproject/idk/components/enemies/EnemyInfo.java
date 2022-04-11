package ca.bcit.comp2522.termproject.idk.components.enemies;

/**
 * Represents a utility class that stores enemy information as constants.
 *
 * @author Nikolay Rozanov.
 * @version 2022
 */
public final class EnemyInfo {
    /**
     * All enemies attack speed.
     */
    public static final int ALL_ENEMIES_ATTACK_SPEED = 4;
    /**
     * FireWizard and DarkWizard attack's damage.
     */
    public static final int WIZARD_DAMAGE = 15;
    /**
     * FireWizard and DarkWizard move speed.
     */
    public static final int WIZARD_MOVE_SPEED = 50;
    /**
     * FireWizards and DarkWizards maximum HP.
     */
    public static final int WIZARD_MAX_HP = 25;
    /**
     * FireWizards and DarkWizards attack's width.
     */
    public static final int WIZARD_ATTACK_WIDTH = 45;
    /**
     * FireWizards and DarkWizards attack's height.
     */
    public static final int WIZARD_ATTACK_HEIGHT = 35;
    /**
     * Motionless enemies' movement speed.
     */
    public static final int MOTIONLESS_MOVE_SPEED = 0;
    /**
     * Flying Eye's maximum HP.
     */
    public static final int FLYING_EYE_MAX_HP = 15;
    /**
     * Flying Eye's attack damage.
     */
    public static final int FLYING_EYE_DAMAGE = 30;
    /**
     *  Boss' move speed.
     */
    public static final int BOSS_MOVE_SPEED = 30;
    /**
     * Flying Eye's path to the projectile attack image.
     */
    public static final String FLYING_EYE_PROJECTILE_IMAGE =
            "Monster_Creatures_Fantasy(Version 1.3)/Flying eye/eye-projectile.png";
    /**
     * Boss' projectile damage.
     */
    public static final int BOSS_PROJECTILE_DAMAGE = 20;
    /**
     * Boss' path to the projectile attack image.
     */
    public static final String BOSS_PROJECTILE_IMAGE = "Mecha-stone Golem 0.1/weapon PNG/arm_projectile.png";
    /**
     * Boss' melee attack damage.
     */
    public static final int BOSS_ATTACK_DAMAGE = 25;
    /**
     * Boss' melee attack width.
     */
    public static final int BOSS_ATTACK_WIDTH = 20;
    /**
     * Boss' melee attack height.
     */
    public static final int BOSS_ATTACK_HEIGHT = 45;


    private EnemyInfo() { }
}

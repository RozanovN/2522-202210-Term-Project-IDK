package ca.bcit.comp2522.termproject.idk.components.utility;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Represents the potion component.
 *
 * @author Nikolay Rozanov
 * @version 2022
 */
public class PotionComponent extends Component {
    private static final class Effect {
        private static final HashMap<Integer, Consumer<Entity>> EFFECTS_TABLE = generateEffectsTable();

        private Effect() {
        }

        private static Consumer<Entity> getRandomEffect() {
            return EFFECTS_TABLE.get(new Random().nextInt(EFFECTS_TABLE.size()));
        }

        private static HashMap<Integer, Consumer<Entity>> generateEffectsTable() {
            HashMap<Integer, Consumer<Entity>> result = new HashMap<>();
            result.put(0, Effect::increaseMaxHP);
            result.put(1, Effect::restoreHP);
            result.put(2, Effect::increaseAttackDamage);
            return result;
        }

        private static void increaseMaxHP(final Entity entity) {
            HealthIntComponent hp =  entity.getComponent(HealthIntComponent.class);
            hp.setMaxValue(hp.getMaxValue() + 5);
        }

        private static void restoreHP(final Entity entity) {
            HealthIntComponent hp =  entity.getComponent(HealthIntComponent.class);
            hp.setValue(hp.getMaxValue());
        }

        private static void increaseAttackDamage(final Entity entity) {
            AttackComponent damage = entity.getComponent(AttackComponent.class);
            damage.changeDamageByValue(2);
        }
    }
    private final Consumer<Entity> effect;
    private final String imagePath;

    public PotionComponent() {
        effect = Effect.getRandomEffect();
        imagePath = getRandomImagePath();
    }

    private String getRandomImagePath() {
        return "48 Free Magic Potions Pixel Art Icons/PNG/Transparent/Icon" + new Random().nextInt(1, 49)
                + ".png";
    }

    public Consumer<Entity> getEffect() {
        return effect;
    }

    public String getImagePath() {
        return imagePath;
    }
}



package eu.iteije.kitpvp.pluginutils;

import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

public enum PotionTypes {

    HEAL(PotionEffectType.HEAL, Color.fromRGB(255, 38, 67)),
    REGENERATION(PotionEffectType.REGENERATION, Color.fromRGB(242, 56, 146)),
    INCREASE_DAMAGE(PotionEffectType.INCREASE_DAMAGE, Color.fromRGB(156, 20, 38)),
    SPEED(PotionEffectType.SPEED, Color.fromRGB(104, 142, 237));

    private final PotionEffectType type;
    private final Color color;

    PotionTypes(PotionEffectType type, Color color) {
        this.type = type;
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public PotionEffectType getType() {
        return this.type;
    }

    public static PotionTypes getByEffect(PotionEffectType effectType) {
        if (effectType == null) return null;

        for (PotionTypes type : values()) {
            if (type.getType().equals(effectType)) {
                return type;
            }
        }

        return null;
    }
}

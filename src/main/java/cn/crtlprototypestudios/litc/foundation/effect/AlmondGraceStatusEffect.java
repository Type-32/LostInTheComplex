package cn.crtlprototypestudios.litc.foundation.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class AlmondGraceStatusEffect extends StatusEffect {
    public AlmondGraceStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 13545855);
        this.addAttributeModifier(
                EntityAttributes.GENERIC_MOVEMENT_SPEED,
                Identifier.ofVanilla("effect.speed"),
                0.05,
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getHealth() < entity.getMaxHealth()) {
            entity.heal(0.25F);
        }
        return true;
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 50 >> amplifier; // This is the similar to that of 50*(1/2)^statusEffect = i, but much more different.... idk how this works.
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }
}

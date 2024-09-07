package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.foundation.effect.AlmondGraceStatusEffect;
import cn.crtlprototypestudios.litc.utility.ReferenceRegistryEntry;
import cn.crtlprototypestudios.litc.utility.RegistryEntry;
import cn.crtlprototypestudios.litc.utility.RegistryHelper;
import net.minecraft.entity.effect.StatusEffect;

public class ModStatusEffects {
    public static final ReferenceRegistryEntry<StatusEffect> ALMOND_GRACE = RegistryHelper.statusEffect("almond_grace", AlmondGraceStatusEffect::new);

    public static void register(){

    }
}

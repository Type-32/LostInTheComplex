package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.foundation.effect.AlmondGraceStatusEffect;
import cn.crtlprototypestudios.litc.utility.RegistryEntry;
import cn.crtlprototypestudios.litc.utility.RegistryHelper;

public class ModStatusEffects {
    public static final RegistryEntry<AlmondGraceStatusEffect> ALMOND_GRACE = RegistryHelper.statusEffect("almond_grace", AlmondGraceStatusEffect::new);

    public static void register(){

    }
}

package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.utility.RegistryEntry;
import cn.crtlprototypestudios.litc.utility.RegistryHelper;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModPotions {
    public static final RegistryEntry<Potion> ALMOND_GRACE_POTION = RegistryHelper.potion("almond_grace")
            .effect(ModStatusEffects.ALMOND_GRACE.get(), 1200)
            .build();

    public static void register(){

    }

    public static net.minecraft.registry.entry.RegistryEntry<Potion> find(Potion potion){
        return Registries.POTION.getEntry(potion);
    }

    public static Identifier findId(Potion potion){
        return Registries.POTION.getId(potion);
    }
}

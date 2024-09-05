package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.utility.RegistryEntry;
import cn.crtlprototypestudios.litc.utility.RegistryHelper;
import net.minecraft.sound.SoundEvent;

public class ModSoundEvents {
    public static final RegistryEntry<SoundEvent> BLOCK_LOOT_CRATE_OPEN = RegistryHelper.soundEvent("block.loot_crate.open");
    public static final RegistryEntry<SoundEvent> BLOCK_LOOT_CRATE_CLOSE = RegistryHelper.soundEvent("block.loot_crate.close");

    public static void register(){

    }
}

package cn.crtlprototypestudios.litc;

import cn.crtlprototypestudios.litc.foundation.*;
import cn.crtlprototypestudios.litc.foundation.custom.ModProperties;
import cn.crtlprototypestudios.litc.utility.Reference;
import cn.crtlprototypestudios.litc.utility.RegistryHelper;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LostInTheComplex implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(LostInTheComplex.class);

    @Override
    public void onInitialize() {
        ModProperties.register();
        ModCodecs.register();
        ModComponents.register();
        ModStatusEffects.register();
        ModPotions.register();
        ModStats.register();
        ModSoundEvents.register();
        ModFluids.register();
        ModBlocks.register();
        ModBlockEntities.register();
        ModItems.register();
        ModItemGroups.register();

        RegistryHelper.registerAll(false);
        LOGGER.info("Loaded LostInTheComplex mod!");
    }
}

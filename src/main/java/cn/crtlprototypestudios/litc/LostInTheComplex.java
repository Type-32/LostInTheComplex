package cn.crtlprototypestudios.litc;

import cn.crtlprototypestudios.litc.foundation.ModBlocks;
import cn.crtlprototypestudios.litc.foundation.ModComponents;
import cn.crtlprototypestudios.litc.foundation.ModItems;
import cn.crtlprototypestudios.litc.utility.Reference;
import cn.crtlprototypestudios.litc.utility.RegistryHelper;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LostInTheComplex implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(LostInTheComplex.class);

    @Override
    public void onInitialize() {
        ModComponents.register();
        ModBlocks.register();
        ModItems.register();

        RegistryHelper.registerAll(Reference.MOD_ID);
        LOGGER.info("Loaded LostInTheComplex mod!");
    }
}

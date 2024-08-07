package cn.crtlprototypestudios.litc;

import cn.crtlprototypestudios.litc.foundation.ModBlocks;
import cn.crtlprototypestudios.litc.foundation.ModItems;
import cn.crtlprototypestudios.litc.utility.RegisterHelper;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LostInTheComplex implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(LostInTheComplex.class);

    @Override
    public void onInitialize() {
        ModBlocks.load();
        ModItems.load();

        RegisterHelper.registerAll();
        LOGGER.info("Loaded LostInTheComplex mod!");
    }
}

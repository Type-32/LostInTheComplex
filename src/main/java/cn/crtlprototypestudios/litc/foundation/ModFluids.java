package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.foundation.fluid.AlmondWaterFluid;
import cn.crtlprototypestudios.litc.utility.FluidEntry;
import cn.crtlprototypestudios.litc.utility.RegistryEntry;
import cn.crtlprototypestudios.litc.utility.RegistryHelper;
import net.minecraft.block.Blocks;

public class ModFluids {
    // Almond Water Fluids
    public static FluidEntry<AlmondWaterFluid> ALMOND_WATER = RegistryHelper.fluid("almond_water", AlmondWaterFluid::new)
            .still(AlmondWaterFluid.Still::new)
            .flowing(AlmondWaterFluid.Flowing::new)
            .block(AlmondWaterFluid.Block::new)
            .blockSettings(Blocks.WATER)
            .build();

    public static void register(){

    }
}

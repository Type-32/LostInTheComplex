package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.foundation.fluid.AlmondWaterFluid;
import cn.crtlprototypestudios.litc.utility.FluidEntry;
import cn.crtlprototypestudios.litc.utility.RegistryHelper;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.Registries;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModFluids {
    // Almond Water Fluids
    public static FluidEntry<AlmondWaterFluid> ALMOND_WATER = RegistryHelper.fluid("almond_water", AlmondWaterFluid.Still::new, AlmondWaterFluid.Flowing::new)
            .block(AlmondWaterFluid.Block::new)
            .blockSettings(Blocks.WATER)
            .blockSettings(settings -> settings.replaceable().liquid())
            .build();

    public static void register(){
//        Fluids
    }

    public static Identifier findId(Fluid fluid){
        return Registries.FLUID.getId(fluid);
    }
}

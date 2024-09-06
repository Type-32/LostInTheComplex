package cn.crtlprototypestudios.litc.client.rendering;

import cn.crtlprototypestudios.litc.LostInTheComplex;
import cn.crtlprototypestudios.litc.foundation.ModFluids;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.util.Identifier;

public class FluidRendering {
    public static void register(){
        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.ALMOND_WATER.getFluid(), ModFluids.ALMOND_WATER.getFlowing(), new SimpleFluidRenderHandler(
                Identifier.of("minecraft:block/water_still"),
                Identifier.of("litc:block/almond_water_flow"),
                0xF3DD83
        ));
    }
}

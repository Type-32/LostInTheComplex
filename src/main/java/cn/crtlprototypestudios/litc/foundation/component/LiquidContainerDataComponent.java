package cn.crtlprototypestudios.litc.foundation.component;

import cn.crtlprototypestudios.litc.foundation.ModFluids;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public record LiquidContainerDataComponent(int amount, int max, boolean replenishable, Identifier liquid, boolean clearEffectsOnEmpty) {
    public static final LiquidContainerDataComponent DEFAULT_EMPTY = new LiquidContainerDataComponent(0, 3, true, ModFluids.findId(Fluids.EMPTY), true);
    public boolean hasLiquid(){
        return !liquid.equals(ModFluids.findId(Fluids.EMPTY));
    }

    public Identifier getEmptyLiquid(){
        return ModFluids.findId(Fluids.EMPTY);
    }

    public boolean isEmpty(){
        return amount <= 0 || !hasLiquid();
    }

    public boolean matchesLiquid(Fluid fluid){
        return ModFluids.findId(fluid).equals(liquid);
    }

    public boolean matchesLiquid(FluidState fluid){
        return ModFluids.findId(fluid.getFluid()).equals(liquid);
    }

    public boolean matchesDefaultLiquid(Fluid fluid){
        return ModFluids.findId(fluid.getDefaultState().getFluid()).equals(liquid);
    }

    public boolean matchesDefaultLiquid(FluidState fluid){
        return ModFluids.findId(fluid.getFluid().getDefaultState().getFluid()).equals(liquid);
    }
}

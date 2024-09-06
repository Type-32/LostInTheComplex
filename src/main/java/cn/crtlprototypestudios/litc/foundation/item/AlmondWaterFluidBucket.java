package cn.crtlprototypestudios.litc.foundation.item;

import cn.crtlprototypestudios.litc.foundation.ModFluids;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AlmondWaterFluidBucket extends BucketItem {
    public AlmondWaterFluidBucket(Fluid fluid, Settings settings) {
        super(fluid, settings);
    }

    public AlmondWaterFluidBucket(Settings settings) {
        this(ModFluids.ALMOND_WATER.getFluid(), settings);
    }
}

package cn.crtlprototypestudios.litc.utility;

import net.minecraft.block.Block;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.block.FluidBlock;
import org.jetbrains.annotations.Nullable;

public class FluidEntry<T extends Fluid> {
    private final RegistryEntry<T> stillFluid;
    private final RegistryEntry<T> flowingFluid;
    private final RegistryEntry<? extends Item> bucketItem;
    private final RegistryEntry<? extends Block> block;

    public FluidEntry(RegistryEntry<T> stillFluid,
                      RegistryEntry<T> flowingFluid, RegistryEntry<? extends Item> bucketItem,
                      RegistryEntry<? extends Block> block) {
        this.stillFluid = stillFluid;
        this.flowingFluid = flowingFluid;
        this.bucketItem = bucketItem;
        this.block = block;
    }

    @Nullable
    public T getFluid() {
        return getStill();
    }

    @Nullable
    public T getStill() {
        return stillFluid != null ? stillFluid.get() : null;
    }

    @Nullable
    public T getFlowing() {
        return flowingFluid != null ? flowingFluid.get() : null;
    }

    @Nullable
    public Item getBucket() {
        return bucketItem != null ? bucketItem.get() : null;
    }

    @Nullable
    public Block getBlock() {
        return block != null ? block.get() : null;
    }
}
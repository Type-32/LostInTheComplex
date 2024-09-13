package cn.crtlprototypestudios.litc.foundation.datagen;

import cn.crtlprototypestudios.litc.LostInTheComplex;
import cn.crtlprototypestudios.litc.foundation.ModBlocks;
import cn.crtlprototypestudios.litc.utility.RegistryEntry;
import cn.crtlprototypestudios.litc.utility.RegistryHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.data.client.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        for (RegistryEntry<?> i : RegistryHelper.getDeferredRegisters(Registries.BLOCK)){
            if(i.get() instanceof FluidBlock || i.hasModel())
                continue;
            else if(i.get() instanceof PillarBlock) {
                blockStateModelGenerator.registerLog((PillarBlock) i.get()).log((PillarBlock) i.get());
            } else {
                blockStateModelGenerator.registerSimpleCubeAll((Block) i.get());
            }
//            blockStateModelGenerator.registerRandomHorizontalRotations(MultipartBlockStateSupplier.create(ModBlocks.YELLOW_CARPETED_FLOOR.get()).with(When.create().set(Properties.HORIZONTAL_AXIS, Direction.Axis.X)).);
//            blockStateModelGenerator.registerItemModel((Block) i.get());
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        for (RegistryEntry<?> i : RegistryHelper.getDeferredRegisters(Registries.ITEM)){
            if(i.hasModel()) {
                LostInTheComplex.LOGGER.info("Caught entry {} doesn't need auto model register.", i.get());
                continue;
            }
            if(!(i.get() instanceof BlockItem) && !i.hasModel()) itemModelGenerator.register((Item) i.get(), Models.GENERATED);
        }
    }
}

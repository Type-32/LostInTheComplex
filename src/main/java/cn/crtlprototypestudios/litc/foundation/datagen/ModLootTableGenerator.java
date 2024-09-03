package cn.crtlprototypestudios.litc.foundation.datagen;

import cn.crtlprototypestudios.litc.foundation.ModBlocks;
import cn.crtlprototypestudios.litc.foundation.ModItems;
import cn.crtlprototypestudios.litc.foundation.datagen.utility.LootTableBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLootTableGenerator extends FabricBlockLootTableProvider {
    public ModLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.YELLOW_CARPETED_FLOOR.get());
        addDrop(ModBlocks.WOODEN_WALLPAPER_WALL.get(), simpleRangeItemEntry(ModItems.PLANKS.get(), 1, 4).get());
    }

    public LootTableBuilder simpleRangeItemEntry(Item item, int min, int max) {
        return new LootTableBuilder()
                .pool()
                    .addItem(item)
                        .addFunction(SetCountLootFunction.builder(UniformLootNumberProvider.create(min, max)))
                    .endItem()
                .endPool();
    }
}

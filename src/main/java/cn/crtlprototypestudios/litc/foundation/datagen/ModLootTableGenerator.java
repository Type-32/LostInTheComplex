package cn.crtlprototypestudios.litc.foundation.datagen;

import cn.crtlprototypestudios.litc.LostInTheComplex;
import cn.crtlprototypestudios.litc.foundation.*;
import cn.crtlprototypestudios.litc.foundation.component.LiquidContainerDataComponent;
import cn.crtlprototypestudios.litc.foundation.datagen.utility.LootTableBuilder;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.loader.impl.game.minecraft.MinecraftGameProvider;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.SetComponentsLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.random.Random;

import java.util.concurrent.CompletableFuture;

public class ModLootTableGenerator extends FabricBlockLootTableProvider {
    public ModLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        LostInTheComplex.LOGGER.info("Datagen Generating...");
        addDrop(ModBlocks.YELLOW_CARPETED_FLOOR.get()); // Drop nothing, due to it's copying Bedrock block settings
        addDrop(ModBlocks.WOODEN_WALLPAPER_WALL.get(), ModLootTables.WOODEN_WALLPAPER_WALL_LOOT.get());
        addDrop(ModBlocks.CONCRETE_WALLPAPER_WALL.get(), ModLootTables.CONCRETE_WALLPAPER_WALL_LOOT.get());
        addDrop(ModBlocks.HUMID_YELLOW_CARPETED_FLOOR.get()); // Drops nothing, as it's copying carpeted floor settings
        addDrop(ModBlocks.LOOT_CRATE_BLOCK.get(), ModLootTables.CRATE_BLOCK_LOOT.get());
        LostInTheComplex.LOGGER.info("Datagen Generated.");
    }

    public LootTableBuilder simpleRangeItemEntry(Item item, int min, int max) {
        return new LootTableBuilder()
                .pool()
                    .addItem(item)
                        .setCount(min, max)
                    .endItem()
                .endPool();
    }

    public LootTableBuilder exampleCrateLoot() {
        return new LootTableBuilder()
                .pool()
                    .addItem(ModItems.PLANKS.get())
                        .setCount(1, 3)
                    .endItem()
                .endPool()
                .pool()
                    .addItem(ModItems.ALMOND_WATER_BOTTLE.get())
                        .setComponent(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get(),
                                new LiquidContainerDataComponent(1, 2, false, Registries.FLUID.getId(ModFluids.ALMOND_WATER.getFluid()), false))
                        .weight(50)
                    .endItem()
                    .addItem(ModItems.ALMOND_WATER_BOTTLE.get())
                        .setComponent(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get(),
                                new LiquidContainerDataComponent(2, 2, false, Registries.FLUID.getId(ModFluids.ALMOND_WATER.getFluid()), false))
                        .weight(10)
                    .endItem()
                    .addItem(ModItems.ALMOND_WATER_BOTTLE.get())
                        .setComponent(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get(),
                                new LiquidContainerDataComponent(0, 2, true, Registries.FLUID.getId(ModFluids.ALMOND_WATER.getFluid()), false))
                        .weight(1)
                    .endItem()
                .endPool();
    }
}

package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.foundation.datagen.utility.LootTableBuilder;

public class ModLootTables {
    public static final LootTableBuilder CRATE_BLOCK_LOOT = new LootTableBuilder()
            .pool()
                .addItem(ModItems.PLANKS.get()).setCount(1, 3).endItem()
            .endPool();
    public static final LootTableBuilder WOODEN_WALLPAPER_WALL_LOOT = new LootTableBuilder()
            .pool()
                .addItem(ModItems.PLANKS.get()).setCount(0, 2).endItem()
            .endPool()
            .pool()
                .addItem(ModItems.TORN_WALLPAPER.get()).endItem()
            .endPool();
    public static final LootTableBuilder CONCRETE_WALLPAPER_WALL_LOOT = new LootTableBuilder()
            .pool()
                .addItem(ModItems.CONCRETE_RUBBLE.get()).setCount(1, 3).endItem()
            .endPool()
            .pool()
                .addItem(ModItems.TORN_WALLPAPER.get()).endItem()
            .endPool();

    public static void register(){

    }
}

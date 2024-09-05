package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.foundation.block.LootCrateBlock;
import cn.crtlprototypestudios.litc.foundation.entity.HostileLuminanceEntity;
import cn.crtlprototypestudios.litc.utility.RegistryHelper;
import cn.crtlprototypestudios.litc.utility.RegistryEntry;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.resource.featuretoggle.FeatureFlag;

public class ModBlocks {
    // Blocks
    public static final RegistryEntry<PillarBlock> YELLOW_CARPETED_FLOOR = RegistryHelper.block("yellow_carpeted_floor")
            .settings(Blocks.BEDROCK)
            .settings(settings -> settings.instrument(NoteBlockInstrument.GUITAR).allowsSpawning(HostileLuminanceEntity::spawnPredicate))
            .item()
            .build(PillarBlock::new);
    public static final RegistryEntry<PillarBlock> HUMID_YELLOW_CARPETED_FLOOR = RegistryHelper.block("humid_yellow_carpeted_floor")
            .settings(YELLOW_CARPETED_FLOOR.get())
            .item()
            .build(PillarBlock::new);
    public static final RegistryEntry<PillarBlock> WOODEN_WALLPAPER_WALL = RegistryHelper.block("wooden_wallpaper_wall")
            .settings(settings -> settings.resistance(2.0F).hardness(8.0F).requiresTool().instrument(NoteBlockInstrument.GUITAR))
            .item()
            .build(PillarBlock::new);
    public static final RegistryEntry<PillarBlock> CONCRETE_WALLPAPER_WALL = RegistryHelper.block("concrete_wallpaper_wall")
            .settings(Blocks.OBSIDIAN)
            .settings(settings -> settings.requiresTool().instrument(NoteBlockInstrument.BASS))
            .item()
            .build(PillarBlock::new);
    public static final RegistryEntry<LootCrateBlock> LOOT_CRATE_BLOCK = RegistryHelper.block("loot_crate_block")
            .settings(Blocks.BARREL)
            .item()
            .build(LootCrateBlock::new);

    public static void register(){

    }
}

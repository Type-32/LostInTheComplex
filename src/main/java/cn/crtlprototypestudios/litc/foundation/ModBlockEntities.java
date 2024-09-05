package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.foundation.block.entity.LootCrateBlockEntity;
import cn.crtlprototypestudios.litc.utility.RegistryEntry;
import cn.crtlprototypestudios.litc.utility.RegistryHelper;
import net.minecraft.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final RegistryEntry<BlockEntityType<LootCrateBlockEntity>> LOOT_CRATE_BE = RegistryHelper.blockEntity("loot_crate_be", LootCrateBlockEntity::new, ModBlocks.LOOT_CRATE_BLOCK.get());

    public static void register(){

    }
}

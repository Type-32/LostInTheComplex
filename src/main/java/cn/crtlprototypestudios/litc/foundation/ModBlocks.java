package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.utility.RegisterHelper;
import cn.crtlprototypestudios.litc.utility.RegistryEntry;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;

public class ModBlocks {
    public static final RegistryEntry<Block> CARPETED_FLOOR = RegisterHelper.block("carpeted_floor")
            .settings(settings -> settings.resistance(3.0F).hardness(10.0F).requiresTool().instrument(NoteBlockInstrument.GUITAR))
            .item()
            .build(PillarBlock::new);

    public static void load(){

    }
}

package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.utility.RegistryEntry;
import cn.crtlprototypestudios.litc.utility.RegistryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;

public class ModItemGroups {
    public static final RegistryEntry<ItemGroup> MOD_GROUP = RegistryHelper.itemGroup("litc", ModBlocks.CONCRETE_WALLPAPER_WALL.get().asItem())
            .entries(RegistryHelper.getDeferredRegisters(Registries.ITEM).toArray(new RegistryEntry[0]))
            .build();

    public static void register(){

    }
}

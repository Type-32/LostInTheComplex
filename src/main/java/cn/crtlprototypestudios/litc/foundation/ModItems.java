package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.utility.RegistryHelper;
import cn.crtlprototypestudios.litc.utility.RegistryEntry;
import net.minecraft.item.Item;

public class ModItems {
    public static final RegistryEntry<Item> CHOCOLATE_BAR = RegistryHelper.item("chocolate_bar")
            .settings(settings -> settings.food(ModComponents.CHOCOLATE_BAR_COMPONENT))
            .build();

    public static void register(){

    }
}

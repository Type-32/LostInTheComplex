package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.foundation.component.LiquidContainerDataComponent;
import cn.crtlprototypestudios.litc.foundation.item.LiquidContainerItem;
import cn.crtlprototypestudios.litc.utility.RegistryHelper;
import cn.crtlprototypestudios.litc.utility.RegistryEntry;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class ModItems {
    public static final RegistryEntry<Item> PLANKS = RegistryHelper.item("planks")
            .settings(settings -> settings.maxCount(40).rarity(Rarity.COMMON))
            .build();

    public static final RegistryEntry<Item> CHOCOLATE_BAR = RegistryHelper.item("chocolate_bar")
            .settings(settings -> settings.food(ModComponents.CHOCOLATE_BAR_COMPONENT))
            .build();

    public static final RegistryEntry<LiquidContainerItem> CANTEEN = RegistryHelper.item("canteen")
            .settings(settings -> settings
                    .component(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get(), new LiquidContainerDataComponent(0, 3, true, null)))
            .build(LiquidContainerItem::new);

    public static void register(){

    }
}

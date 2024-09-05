package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.foundation.component.LiquidContainerDataComponent;
import cn.crtlprototypestudios.litc.foundation.item.LiquidContainerItem;
import cn.crtlprototypestudios.litc.utility.RegistryHelper;
import cn.crtlprototypestudios.litc.utility.RegistryEntry;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Rarity;

public class ModItems {
    public static final RegistryEntry<Item> PLANKS = RegistryHelper.item("planks")
            .settings(settings -> settings.maxCount(40).rarity(Rarity.COMMON))
            .build();

    public static final RegistryEntry<Item> CONCRETE_RUBBLE = RegistryHelper.item("concrete_rubble")
            .settings(settings -> settings.rarity(Rarity.COMMON))
            .build();

    public static final RegistryEntry<Item> TORN_WALLPAPER = RegistryHelper.item("torn_wallpaper")
            .settings(settings -> settings.maxCount(72).rarity(Rarity.COMMON))
            .build();

    public static final RegistryEntry<Item> CHOCOLATE_BAR = RegistryHelper.item("chocolate_bar")
            .settings(settings -> settings.food(ModComponents.CHOCOLATE_BAR_COMPONENT))
            .build();

    public static final RegistryEntry<LiquidContainerItem> CANTEEN = RegistryHelper.item("canteen")
            .settings(settings -> settings
                    .component(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get(), new LiquidContainerDataComponent(0, 3, true, null)))
            .build(LiquidContainerItem::new);

    public static final RegistryEntry<LiquidContainerItem> ALMOND_WATER_BOTTLE = RegistryHelper.item("almond_water_bottle")
            .settings(settings -> settings
                    .component(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get(), new LiquidContainerDataComponent(0, 2, false, Registries.FLUID.getId(ModFluids.ALMOND_WATER.getFluid()))))
            .build(LiquidContainerItem::new);

    public static void register(){

    }
}

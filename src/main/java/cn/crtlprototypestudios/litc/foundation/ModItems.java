package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.foundation.component.LiquidContainerDataComponent;
import cn.crtlprototypestudios.litc.foundation.item.AlmondWaterFluidBucket;
import cn.crtlprototypestudios.litc.foundation.item.LiquidContainerItem;
import cn.crtlprototypestudios.litc.utility.RegistryHelper;
import cn.crtlprototypestudios.litc.utility.RegistryEntry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
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

    public static final RegistryEntry<LiquidContainerItem> WOODEN_CANTEEN = RegistryHelper.item("wooden_canteen")
            .settings(settings -> settings
                    .component(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get(), LiquidContainerDataComponent.DEFAULT_EMPTY))
            .hasModel()
            .build(LiquidContainerItem::new);

    public static final RegistryEntry<LiquidContainerItem> ALMOND_WATER_BOTTLE = RegistryHelper.item("almond_water_bottle")
            .component(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get(), new LiquidContainerDataComponent(2, 2, false, ModPotions.findId(ModPotions.ALMOND_GRACE_POTION.asRegistryEntry().value())))
            .component(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(ModPotions.ALMOND_GRACE_POTION.asRegistryEntry()))
            .build(LiquidContainerItem::new);

    public static final RegistryEntry<AlmondWaterFluidBucket> ALMOND_WATER_BUCKET = RegistryHelper.item("almond_water_bucket")
            .build(AlmondWaterFluidBucket::new);

    public static void register(){

    }
}

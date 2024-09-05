package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.foundation.component.LiquidContainerDataComponent;
import cn.crtlprototypestudios.litc.utility.RegistryEntry;
import cn.crtlprototypestudios.litc.utility.RegistryHelper;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Items;

public class ModComponents {
    public static final FoodComponent CHOCOLATE_BAR_COMPONENT = RegistryHelper.foodComponent()
            .settings(s -> s.nutrition(4)
                    .saturationModifier(0.3F)
                    .alwaysEdible())
            .build();

    public static final RegistryEntry<ComponentType<LiquidContainerDataComponent>> LIQUID_CONTAINER_DATA_COMPONENT = RegistryHelper.simpleComponent("liquid_container_data")
            .build(ModCodecs.LIQUID_CONTAINER_DATA_COMPONENT_CODEC);

    public static void register(){

    }
}

package cn.crtlprototypestudios.litc.foundation.components;

import net.minecraft.component.type.FoodComponent;

public class FoodComponents {
    public static final FoodComponent CHOCOLATE_BAR_COMPONENT = new FoodComponent.Builder()
            .nutrition(4)
            .saturationModifier(0.3F)
            .alwaysEdible()
            .build();
}

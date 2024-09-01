package cn.crtlprototypestudios.litc.foundation;

import net.minecraft.component.type.FoodComponent;

public class ModComponents {
    public static final FoodComponent CHOCOLATE_BAR_COMPONENT = new FoodComponent.Builder()
            .nutrition(4)
            .saturationModifier(0.3F)
            .alwaysEdible()
            .build();

    public static void register(){

    }
}

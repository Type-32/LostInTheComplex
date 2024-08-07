package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.foundation.components.FoodComponents;
import cn.crtlprototypestudios.litc.utility.Reference;
import cn.crtlprototypestudios.litc.utility.RegisterHelper;
import cn.crtlprototypestudios.litc.utility.RegistryEntry;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final RegistryEntry<Item> CHOCOLATE_BAR = RegisterHelper.item("chocolate_bar")
            .settings(settings -> settings.food(FoodComponents.CHOCOLATE_BAR_COMPONENT))
            .build();

    public static void load(){

    }
}

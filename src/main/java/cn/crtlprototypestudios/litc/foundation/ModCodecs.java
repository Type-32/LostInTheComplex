package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.foundation.component.LiquidContainerDataComponent;
import cn.crtlprototypestudios.litc.foundation.component.LootCrateDataComponent;
import cn.crtlprototypestudios.litc.utility.RegistryHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;

public class ModCodecs {
    public static final Codec<LiquidContainerDataComponent> LIQUID_CONTAINER_DATA_COMPONENT_CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.INT.fieldOf("amount").forGetter(LiquidContainerDataComponent::amount),
                Codec.INT.fieldOf("max").forGetter(LiquidContainerDataComponent::max),
                Codec.BOOL.fieldOf("replenishable").forGetter(LiquidContainerDataComponent::replenishable),
                Identifier.CODEC.optionalFieldOf("liquid", ModFluids.findId(Fluids.EMPTY)).forGetter(LiquidContainerDataComponent::liquid),
                Codec.BOOL.fieldOf("clearEffectsOnEmpty").forGetter(LiquidContainerDataComponent::clearEffectsOnEmpty)
        ).apply(builder, LiquidContainerDataComponent::new);
    });

    public static final Codec<LootCrateDataComponent> LOOT_CRATE_DATA_COMPONENT_CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.BOOL.optionalFieldOf("regenerate", true).forGetter(LootCrateDataComponent::regenerate),
                Identifier.CODEC.optionalFieldOf("loot_table", RegistryHelper.id("default_loot_crate_loot")).forGetter(LootCrateDataComponent::lootTable)
        ).apply(builder, LootCrateDataComponent::new);
    });

    public static void register(){

    }
}

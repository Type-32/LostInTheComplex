package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.foundation.component.LiquidContainerDataComponent;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

public class ModCodecs {
    public static final Codec<LiquidContainerDataComponent> LIQUID_CONTAINER_DATA_COMPONENT_CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.INT.optionalFieldOf("amount", 0).forGetter(LiquidContainerDataComponent::amount),
                Codec.INT.optionalFieldOf("max", 3).forGetter(LiquidContainerDataComponent::max),
                Codec.BOOL.optionalFieldOf("replenishable", true).forGetter(LiquidContainerDataComponent::replenishable),
                Identifier.CODEC.optionalFieldOf("liquid", null).forGetter(LiquidContainerDataComponent::liquid)
        ).apply(builder, LiquidContainerDataComponent::new);
    });

    public static void register(){

    }
}

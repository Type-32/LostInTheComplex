package cn.crtlprototypestudios.litc.foundation.component;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public record LiquidContainerDataComponent(int amount, int max, boolean replenishable, @Nullable Identifier liquid) {
}

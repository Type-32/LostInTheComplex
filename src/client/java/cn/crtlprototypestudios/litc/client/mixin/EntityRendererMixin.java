package cn.crtlprototypestudios.litc.client.mixin;

import cn.crtlprototypestudios.litc.foundation.ModGameRules;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void doNotRender(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (entity.getWorld().getGameRules().getBoolean(ModGameRules.SHOW_PLAYER_NAMETAGS)) { // If the gamerule is true then don't modify the results of the injected method
            return;
        }

        if (entity instanceof PlayerEntity) { // To prevent that non-player entities' name tags may be falsely unrendered.
            ci.cancel();
        }
    }
}

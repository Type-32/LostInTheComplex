package cn.crtlprototypestudios.litc.mixin;

import cn.crtlprototypestudios.litc.experimental.neural.ssm.SituationalContext;
import cn.crtlprototypestudios.litc.experimental.neural.ssm.SituationalInformation;
import cn.crtlprototypestudios.litc.experimental.ticking.StateScheduler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void tick(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if(player.getServerWorld().isClient) return;

        StateScheduler.tasks
                .stream()
                .filter(tickTask ->
                        (tickTask.situationalContext == SituationalContext.PlayerBased || tickTask.situationalContext == SituationalContext.General)
                                && player.getServerWorld().getTickManager().getStepTicks() % tickTask.runEveryTicks == 0
                )
                .forEach(tickTask -> {
                    tickTask.ssm.process(new SituationalInformation(tickTask.situationalContext, player, player.getServerWorld()));
                });
    }
}

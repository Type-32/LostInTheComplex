package cn.crtlprototypestudios.litc.experimental.neural.ssm;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Optional;

public class SituationalInformation {
    public final Optional<ServerPlayerEntity> serverPlayerEntity;
    public final Optional<ServerWorld> serverWorld;
    public final SituationalContext context;

    public SituationalInformation(SituationalContext context, ServerPlayerEntity serverPlayerEntity, ServerWorld serverWorld) {
        this.serverPlayerEntity = Optional.of(serverPlayerEntity);
        this.serverWorld = Optional.of(serverWorld);
        this.context = context;
    }
}

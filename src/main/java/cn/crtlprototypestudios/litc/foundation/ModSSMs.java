package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.experimental.neural.ssm.SituationalStateMachine;
import cn.crtlprototypestudios.litc.experimental.ticking.StateScheduler;

public class ModSSMs {
    public static final SituationalStateMachine playerSSM = SituationalStateMachine.Builder.create(0.3)
            .addCondition("player_is_on_ground", 0.2, (info) -> {
                return info.serverPlayerEntity.get().isOnGround();
            }, info -> {}, 0.1).build();

    public static void register() {
         StateScheduler.register();
    }
}

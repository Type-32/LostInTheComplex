package cn.crtlprototypestudios.litc.foundation;

import cn.crtlprototypestudios.litc.experimental.neural.ssm.SituationCondition;
import cn.crtlprototypestudios.litc.experimental.neural.ssm.SituationalStateMachine;
import cn.crtlprototypestudios.litc.experimental.ticking.StateScheduler;

public class ModSSMs {
    public static final SituationalStateMachine playerSSM = SituationalStateMachine.Builder.create(0.3)
            .addCondition("playerAlone", 0.2,
                    (info) -> info.serverPlayerEntity.filter(serverPlayerEntity -> SituationCondition.Predicates.isThisPlayerUnseenAndAlone(info) && serverPlayerEntity.isOnGround()).isPresent(),
                    info -> {}, 0.1)
            .build();

    public static void register() {
         StateScheduler.register();
    }
}

package cn.crtlprototypestudios.litc.experimental.ticking;

import cn.crtlprototypestudios.litc.experimental.neural.ssm.SituationalContext;
import cn.crtlprototypestudios.litc.experimental.neural.ssm.SituationalStateMachine;

public class TickTask {
    public final int runEveryTicks;
    public final SituationalStateMachine ssm;
    public final SituationalContext situationalContext;
    public TickTask(int runEveryTicks, SituationalStateMachine ssm, SituationalContext context) {
        this.runEveryTicks = runEveryTicks;
        this.ssm = ssm;
        this.situationalContext = context;
    }
}

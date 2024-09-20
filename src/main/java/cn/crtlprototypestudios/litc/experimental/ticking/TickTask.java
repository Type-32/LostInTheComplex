package cn.crtlprototypestudios.litc.experimental.ticking;

import cn.crtlprototypestudios.litc.experimental.neural.ssm.SituationalStateMachine;

public class TickTask {
    public final int runEveryTicks;
    public final SituationalStateMachine ssm;
    public TickTask(int runEveryTicks, SituationalStateMachine ssm) {
        this.runEveryTicks = runEveryTicks;
        this.ssm = ssm;
    }
}

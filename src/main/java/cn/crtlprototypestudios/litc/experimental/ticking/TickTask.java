package cn.crtlprototypestudios.litc.experimental.ticking;

public class TickTask {
    public final int runEveryTicks;
    public final Runnable runnable;
    public TickTask(int runEveryTicks, Runnable runnable) {
        this.runEveryTicks = runEveryTicks;
        this.runnable = runnable;
    }
}

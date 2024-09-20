package cn.crtlprototypestudios.litc.experimental.ticking;

import cn.crtlprototypestudios.litc.experimental.neural.ssm.SituationalStateMachine;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.ArrayList;
import java.util.List;

public class StateScheduler {
    public static List<TickTask> tasks = new ArrayList<>();

    public static void schedule(int runEveryTicks, SituationalStateMachine ssm) {
        tasks.add(new TickTask(runEveryTicks, ssm));
    }

    public static void register() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for(TickTask task : StateScheduler.tasks) {
                if (server.getTicks() % task.runEveryTicks == 0) {
                    task.ssm.process();
                }
            }
        });
    }
}
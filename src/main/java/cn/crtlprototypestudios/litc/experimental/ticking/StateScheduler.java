package cn.crtlprototypestudios.litc.experimental.ticking;

import cn.crtlprototypestudios.litc.experimental.neural.ssm.SituationalContext;
import cn.crtlprototypestudios.litc.experimental.neural.ssm.SituationalInformation;
import cn.crtlprototypestudios.litc.experimental.neural.ssm.SituationalStateMachine;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.ArrayList;
import java.util.List;

public class StateScheduler {
    public static List<TickTask> tasks = new ArrayList<>();

    public static void schedule(int runEveryTicks, SituationalStateMachine ssm, SituationalContext context) {
        tasks.add(new TickTask(runEveryTicks, ssm, context));
    }

    public static void register() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for(TickTask task : StateScheduler.tasks) {
                if (task.situationalContext == SituationalContext.WorldBased && server.getTicks() % task.runEveryTicks == 0) {
                    server.getWorlds().forEach(world -> {
                        task.ssm.process(new SituationalInformation(SituationalContext.WorldBased, null, world));
                    });
                }
            }
        });


    }
}

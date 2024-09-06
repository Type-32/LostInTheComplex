package cn.crtlprototypestudios.litc.foundation.custom.impl.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface MiddayMidnightBroadcastEvent {
    void onCallback();
    Event<MiddayMidnightBroadcastEvent> EVENT = EventFactory.createArrayBacked(MiddayMidnightBroadcastEvent.class, (listeners) -> () -> {
        for (MiddayMidnightBroadcastEvent listener : listeners) {
            listener.onCallback();
        }
    });
}

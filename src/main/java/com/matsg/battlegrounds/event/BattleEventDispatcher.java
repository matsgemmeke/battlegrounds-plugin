package com.matsg.battlegrounds.event;

import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.event.EventChannel;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;

public class BattleEventDispatcher implements EventDispatcher {

    private Map<Class<? extends Event>, EventChannel> eventChannels;

    public BattleEventDispatcher() {
        this.eventChannels = new HashMap<>();
    }

    public void dispatchEvent(Event event) {
        eventChannels.get(event.getClass()).handleEvent(event);
    }

    public void registerEventChannel(Class<? extends Event> eventClass, EventChannel eventChannel) {
        if (eventChannels.containsKey(eventClass)) {
            throw new EventHandlingException("Event class " + eventClass.getSimpleName() + " already registered");
        }
        eventChannels.put(eventClass, eventChannel);
    }

    public void unregisterEventChannel(Class<? extends Event> eventClass) {
        eventChannels.remove(eventClass);
    }
}

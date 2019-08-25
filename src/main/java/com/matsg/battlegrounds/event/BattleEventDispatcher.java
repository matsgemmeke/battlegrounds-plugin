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
        if (!eventChannels.containsKey(event.getClass())) {
            return;
        }
        eventChannels.get(event.getClass()).handleEvent(event);
    }

    public <T extends Event> void registerEventChannel(Class<T> eventClass, EventChannel<T> eventChannel) {
        if (eventChannels.containsKey(eventClass)) {
            // If an event channel for the event class already exists, add the event handlers to the existing channel
            EventChannel existingChannel = eventChannels.get(eventClass);
            existingChannel.addEventChannel(eventChannel);
        } else {
            eventChannels.put(eventClass, eventChannel);
        }
    }

    public void unregisterEventChannel(Class<? extends Event> eventClass) {
        if (!eventChannels.containsKey(eventClass)) {
            return;
        }

        eventChannels.remove(eventClass);
    }
}

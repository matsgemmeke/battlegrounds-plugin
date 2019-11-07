package com.matsg.battlegrounds.event;

import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.event.EventChannel;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.Map;

public class BattleEventDispatcher implements EventDispatcher {

    private Map<Class<? extends Event>, EventChannel> eventChannels;
    private PluginManager pluginManager;

    public BattleEventDispatcher(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
        this.eventChannels = new HashMap<>();
    }

    public void dispatchExternalEvent(Event event) {
        // Dispatch the event internally so the plugin does not have to listen for its own events
        dispatchInternalEvent(event);
        // Call the outgoing event to the plugin manager
        pluginManager.callEvent(event);
    }

    public void dispatchInternalEvent(Event event) {
        if (!eventChannels.containsKey(event.getClass())) {
            return;
        }
        eventChannels.get(event.getClass()).handleEvent(event);
    }

    public int getEventChannelCount() {
        return eventChannels.size();
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

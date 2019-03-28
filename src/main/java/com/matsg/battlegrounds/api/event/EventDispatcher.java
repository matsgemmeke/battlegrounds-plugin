package com.matsg.battlegrounds.api.event;

import org.bukkit.event.Event;

public interface EventDispatcher {

    /**
     * Dispatches an event to its corresponding event channel.
     *
     * @param event The event to be dispatched.
     */
    void dispatchEvent(Event event);

    /**
     * Registers an event channel.
     *
     * @param eventClass The event class that the event channel handles.
     * @param eventChannel The event channel instance.
     */
    void registerEventChannel(Class<? extends Event> eventClass, EventChannel eventChannel);

    /**
     * Unregisters an event channel.
     *
     * @param eventClass The event class that the event channel handles.
     */
    void unregisterEventChannel(Class<? extends Event> eventClass);
}

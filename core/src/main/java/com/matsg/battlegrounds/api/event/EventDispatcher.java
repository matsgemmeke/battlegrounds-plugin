package com.matsg.battlegrounds.api.event;

import org.bukkit.event.Event;

public interface EventDispatcher {

    /**
     * Dispatches an event that gets sent to its corresponding event channel and to
     * the server as an outgoing event.
     *
     * @param event the event to be dispatched
     */
    void dispatchExternalEvent(Event event);

    /**
     * Dispatches an event that gets sent only to its corresponding event channel.
     *
     * @param event the event to be dispatched
     */
    void dispatchInternalEvent(Event event);

    /**
     * Gets the amount of registered event channels.
     *
     * @return the event channel count
     */
    int getEventChannelCount();

    /**
     * Registers an event channel.
     *
     * @param eventClass the event class that the event channel handles
     * @param eventChannel the event channel instance
     */
    <T extends Event> void registerEventChannel(Class<T> eventClass, EventChannel<T> eventChannel);

    /**
     * Unregisters an event channel.
     *
     * @param eventClass the event class that the event channel handles
     */
    void unregisterEventChannel(Class<? extends Event> eventClass);
}

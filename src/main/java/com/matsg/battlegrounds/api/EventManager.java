package com.matsg.battlegrounds.api;

import com.matsg.battlegrounds.api.event.handler.EventHandler;
import org.bukkit.event.Event;

public interface EventManager {

    /**
     * Registers an event handler.
     *
     * @param eventClass The event class that the event handler handles
     * @param eventHandler The event handler
     */
    void addEventHandler(Class<? extends Event> eventClass, EventHandler eventHandler);

    /**
     * Handles an event.
     *
     * @param event The event
     */
    void handleEvent(Event event);

    /**
     * Removes a registered event handler.
     *
     * @param eventClass The event class whose handler should be removed
     */
    void removeEventHandler(Class<? extends Event> eventClass);
}

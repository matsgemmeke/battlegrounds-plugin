package com.matsg.battlegrounds.api;

import com.matsg.battlegrounds.api.game.EventHandler;
import org.bukkit.event.Event;

public interface EventManager {

    /**
     * Calls an event and sends it to all listening event handlers.
     *
     * @param event The event to be called
     */
    void callEvent(Event event);

    /**
     * Deregisters a listening event handler.
     *
     * @param eventHandler The event handler to deregister
     */
    void deregisterEventHandler(EventHandler eventHandler);

    /**
     * Registers a new event handler
     *
     * @param eventHandler The event handler to register
     */
    void registerEventHandler(EventHandler eventHandler);
}
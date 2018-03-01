package com.matsg.battlegrounds.api;

import com.matsg.battlegrounds.api.game.EventHandler;
import org.bukkit.event.Event;

public interface EventManager {

    void callEvent(Event event);

    void deregisterEventHandler(EventHandler eventHandler);

    void registerEventHandler(EventHandler eventHandler);
}
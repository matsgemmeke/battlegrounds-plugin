package com.matsg.battlegrounds.api.event.handler;

import org.bukkit.event.Event;

public interface EventHandler<T extends Event> {

    void handle(T event);
}
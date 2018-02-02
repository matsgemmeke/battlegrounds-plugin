package com.matsg.battlegrounds.api.game;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public interface EventHandler extends Listener {

    void callEvent(Event event);
}
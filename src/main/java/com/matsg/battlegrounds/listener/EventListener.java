package com.matsg.battlegrounds.listener;

import com.matsg.battlegrounds.api.Battlegrounds;
import org.bukkit.event.*;
import org.bukkit.plugin.RegisteredListener;

public class EventListener implements Listener {

    private Battlegrounds plugin;

    public EventListener(Battlegrounds plugin) {
        this.plugin = plugin;

        RegisteredListener registeredListener = new RegisteredListener(this, (listener, event) -> onEvent(event), EventPriority.NORMAL, plugin, false);

        for (HandlerList handler : HandlerList.getHandlerLists()) {
            handler.register(registeredListener);
        }
    }

    public void onEvent(Event event) {
        plugin.getEventManager().callEvent(event);
    }
}
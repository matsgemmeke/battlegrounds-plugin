package com.matsg.battlegrounds.listener;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.util.BattleRunnable;
import org.bukkit.event.*;
import org.bukkit.plugin.RegisteredListener;

public class EventListener implements Listener {

    private Battlegrounds plugin;

    public EventListener(Battlegrounds plugin) {
        this.plugin = plugin;

        new BattleRunnable() {
            public void run() {
                RegisteredListener registeredListener = new RegisteredListener(EventListener.this, (listener, event) -> onEvent(event), EventPriority.NORMAL, plugin, false);

                for (HandlerList handler : HandlerList.getHandlerLists()) {
                    handler.register(registeredListener);
                }
            }
        }.runTaskLater(1);
    }

    private void onEvent(Event event) {
        plugin.getEventManager().callEvent(event);
    }
}
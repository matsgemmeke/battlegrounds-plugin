package com.matsg.battlegrounds.event;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.EventChannel;
import com.matsg.battlegrounds.event.handler.PlayerSwapHandItemsEventHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerSwapItemListener implements Listener {

    private Battlegrounds plugin;

    public PlayerSwapItemListener(Battlegrounds plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        plugin.getEventDispatcher().registerEventChannel(PlayerSwapHandItemsEvent.class, new EventChannel<>(
                new PlayerSwapHandItemsEventHandler(plugin)
        ));
    }

    @EventHandler
    public void onPlayerItemSwap(PlayerSwapHandItemsEvent event) {
        plugin.getEventDispatcher().dispatchEvent(event);
    }
}

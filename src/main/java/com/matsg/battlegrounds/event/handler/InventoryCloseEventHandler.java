package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.handler.EventHandler;
import com.matsg.battlegrounds.gui.View;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseEventHandler implements EventHandler<InventoryCloseEvent> {

    private Battlegrounds plugin;

    public InventoryCloseEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof View) || ((View) event.getInventory().getHolder()).onClose()) {
            return;
        }

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                event.getPlayer().openInventory(event.getInventory());
            }
        }, 1);
    }
}

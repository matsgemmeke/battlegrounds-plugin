package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.gui.view.View;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseEventHandler implements EventHandler<InventoryCloseEvent> {

    public void handle(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof View)) {
            return;
        }

        ((View) event.getInventory().getHolder()).onClose((Player) event.getPlayer());
    }
}

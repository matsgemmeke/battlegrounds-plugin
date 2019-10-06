package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.gui.view.View;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickEventHandler implements EventHandler<InventoryClickEvent> {

    public void handle(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (!(event.getInventory().getHolder() instanceof View)) {
            return;
        }

        event.setCancelled(true);

        ((View) event.getInventory().getHolder()).onClick(player, itemStack, event.getClick());
    }
}

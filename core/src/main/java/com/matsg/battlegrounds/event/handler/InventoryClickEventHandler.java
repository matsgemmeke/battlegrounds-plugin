package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickEventHandler implements EventHandler<InventoryClickEvent> {

    private Battlegrounds plugin;

    public InventoryClickEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (plugin.getGameManager().getGame(player) == null || event.getClickedInventory() == null || !event.getClickedInventory().equals(player.getInventory())) {
            return;
        }

        event.setCancelled(true);
    }
}

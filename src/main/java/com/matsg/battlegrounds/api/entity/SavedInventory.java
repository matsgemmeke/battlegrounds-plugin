package com.matsg.battlegrounds.api.entity;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface SavedInventory {

    /**
     * Gets the inventory that was saved.
     *
     * @return the saved inventory
     */
    Inventory getInventory();

    /**
     * Restores the inventory to a player.
     *
     * @param player the player to restore the inventory to
     */
    void restore(Player player);
}

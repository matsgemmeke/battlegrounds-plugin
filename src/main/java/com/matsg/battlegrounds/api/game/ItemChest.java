package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.item.TransactionItem;
import org.bukkit.block.Chest;

public interface ItemChest extends ArenaComponent, Interactable, Lockable, Priceable, Watchable {

    /**
     * Gets the chest block of the item chest.
     *
     * @return The chest block.
     */
    Chest getChest();

    /**
     * Gets the item the item chests sells.
     *
     * @return The item chest's item.
     */
    TransactionItem getItem();
}

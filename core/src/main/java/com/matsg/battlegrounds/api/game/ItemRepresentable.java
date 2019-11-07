package com.matsg.battlegrounds.api.game;

import org.bukkit.inventory.ItemStack;

/**
 * Represents an object that can be represented as an item.
 */
public interface ItemRepresentable {

    /**
     * Converts the object into an item stack that represents it.
     *
     * @return an item stack
     */
    ItemStack toItemStack();
}

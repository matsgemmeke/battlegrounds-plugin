package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.inventory.ItemStack;

public interface ItemRegistry {

    /**
     * Gets the items currently registered.
     *
     * @return the items in the registry
     */
    Iterable<Item> getItems();

    /**
     * Adds an item to the registry.
     *
     * @param item the item to add
     */
    void addItem(Item item);

    /**
     * Clears the registry.
     */
    void clear();

    /**
     * Gets an item by their item stack.
     *
     * @param itemStack the item stack
     * @return the corresponding item or null if none were found
     */
    Item getItem(ItemStack itemStack);

    /**
     * Gets an item by their item stack, ignoring its meta data.
     *
     * @param itemStack the item stack
     * @return the corresponding item or null if none were found
     */
    Item getItemIgnoreMetadata(ItemStack itemStack);

    /**
     * Gets a weapon by their item stack.
     *
     * @param gamePlayer the player who owns the weapon
     * @param itemStack the item stack
     * @return the corresponding weapon or null if none were found
     */
    Weapon getWeapon(GamePlayer gamePlayer, ItemStack itemStack);

    /**
     * Gets a weapon by their item stack, ignoring its meta data.
     *
     * @param gamePlayer the player who owns the weapon
     * @param itemStack the item stack
     * @return the corresponding weapon or null if none were found
     */
    Weapon getWeaponIgnoreMetadata(GamePlayer gamePlayer, ItemStack itemStack);

    /**
     * Removes an item from the registry.
     *
     * @param item the item to remove
     */
    void removeItem(Item item);
}

package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.GenericAttribute;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public interface Item extends Cloneable, Comparable<Item> {

    /**
     * Gets the game of the item.
     *
     * @return the item's game
     */
    Game getGame();

    /**
     * Sets the game of the item.
     *
     * @param game the item's game
     */
    void setGame(Game game);

    /**
     * Gets the item stack of the item.
     *
     * @return the item's item stack
     */
    ItemStack getItemStack();

    /**
     * Sets the item stack of the item.
     *
     * @param itemStack the item's item stack
     */
    void setItemStack(ItemStack itemStack);

    /**
     * Gets the metadata of the item.
     *
     * @return the item metadata
     */
    ItemMetadata getMetadata();

    /**
     * Sets the metadata of the item.
     *
     * @param metadata the item metadata
     */
    void setMetadata(ItemMetadata metadata);

    /**
     * Gets an item attribute by a certain id.
     *
     * @param id the id of the attribute
     * @return the corresponding attribute or null if none were found
     */
    GenericAttribute getAttribute(String id);

    /**
     * Clones the item.
     *
     * @return a clone of the item
     */
    Item clone();

    /**
     * Handles a performed left click on the item.
     *
     * @param gamePlayer the player who left-clicked the item
     * @param event the event that led to the click interaction
     */
    void onLeftClick(GamePlayer gamePlayer, PlayerInteractEvent event);

    /**
     * Handles a performed right click on the item.
     *
     * @param gamePlayer the player who right-clicked the item
     * @param event the event that led to the click interaction
     */
    void onRightClick(GamePlayer gamePlayer, PlayerInteractEvent event);

    /**
     * Handles a performed swap action on the item.
     *
     * @param gamePlayer the player who swapped the item
     * @param event the event that led to the swap interaction
     */
    void onSwap(GamePlayer gamePlayer, PlayerSwapHandItemsEvent event);

    /**
     * Handles a performed switch action on the item.
     *
     * @param gamePlayer the player who switched the item
     * @param event the event that led to the switch interaction
     */
    void onSwitch(GamePlayer gamePlayer, PlayerItemHeldEvent event);

    /**
     * Removes the item's requisites.
     */
    void remove();

    /**
     * Sets an item attribute by a certain id.
     *
     * @param id the id of the attribute
     * @param attribute the attribute
     */
    void setAttribute(String id, GenericAttribute attribute);

    /**
     * Updates the item's requisites.
     *
     * @return whether the item has updated
     */
    boolean update();
}

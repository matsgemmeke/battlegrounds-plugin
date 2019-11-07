package com.matsg.battlegrounds.api;

import org.bukkit.entity.Player;

public interface SelectionManager {

    /**
     * Gets the selection of a certain player. Returns null if the player has not made a selection.
     *
     * @param player the player
     * @return the selection of the player
     */
    Selection getSelection(Player player);

    /**
     * Sets the selection of a certain player.
     *
     * @param player the player
     * @param selection the selection of the player
     */
    void setSelection(Player player, Selection selection);
}

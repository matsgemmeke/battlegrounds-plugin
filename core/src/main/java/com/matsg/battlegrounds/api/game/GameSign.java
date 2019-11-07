package com.matsg.battlegrounds.api.game;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public interface GameSign {

    /**
     * Gets the sign block of the game sign.
     *
     * @return the sign block
     */
    Sign getSign();

    /**
     * Handles a click from a player.
     *
     * @param player the player who performed the click
     */
    void click(Player player);

    /**
     * Updates the display of the game sign.
     *
     * @return whether the sign was updated
     */
    boolean update();
}

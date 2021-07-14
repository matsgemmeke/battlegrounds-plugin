package com.matsg.battlegrounds.api.entity;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public interface PlayerState {

    /**
     * Gets whether the player can interact with other things in this state.
     *
     * @return whether the player in this state can interact with other things
     */
    boolean canInteract();

    /**
     * Gets whether the player can move in this state.
     *
     * @return whether the player in this state can move.
     */
    boolean canMove();

    /**
     * Gets chat color that represents the player state.
     *
     * @return the state's chat color
     */
    @NotNull
    ChatColor getChatColor();

    /**
     * Gets whether the player in this state is alive.
     *
     * @return whether the player in this state is alive
     */
    boolean isAlive();

    /**
     * Applies the state's attributes to the player.
     */
    void apply();

    /**
     * Removes the state's attributes from the player.
     */
    void remove();
}

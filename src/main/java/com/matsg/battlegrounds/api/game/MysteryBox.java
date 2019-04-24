package com.matsg.battlegrounds.api.game;

import org.bukkit.block.Block;

public interface MysteryBox extends ArenaComponent, Interactable, Lockable, Priceable, Watchable {

    /**
     * Gets the left side block of the double chest.
     *
     * @return The left block.
     */
    Block getLeftSide();

    /**
     * Gets the right side block of the double chest.
     *
     * @return The right block.
     */
    Block getRightSide();

    /**
     * Gets whether the mystery box is currently active and available for use.
     *
     * @return Whether the mystery box is active.
     */
    boolean isActive();

    /**
     * Sets whether the mystery box is currently active and available for use.
     *
     * @return Whether the mystery box should be active.
     */
    void setActive(boolean active);
}

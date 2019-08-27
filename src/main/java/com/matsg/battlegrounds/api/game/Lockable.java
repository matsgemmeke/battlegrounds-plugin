package com.matsg.battlegrounds.api.game;

/**
 * Represents a component that be locked depending on the state of the game.
 */
public interface Lockable {

    /**
     * Gets whether the object is locked.
     *
     * @return true if the object is locked, otherwise false
     */
    boolean isLocked();

    /**
     * Sets whether the object is locked.
     *
     * @param locked whether the object is locked
     */
    void setLocked(boolean locked);
}

package com.matsg.battlegrounds.api.game;

public interface Lockable {

    /**
     * Gets whether the object is locked.
     *
     * @return True if the object is locked, otherwise false.
     */
    boolean isLocked();

    /**
     * Sets whether the object is locked.
     *
     * @param locked Whether the object is locked.
     */
    void setLocked(boolean locked);
}

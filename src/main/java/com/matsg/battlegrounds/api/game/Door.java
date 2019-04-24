package com.matsg.battlegrounds.api.game;

public interface Door extends ArenaComponent, Extent, Interactable, Lockable, Watchable {

    /**
     * Gets the section of which the door guards.
     *
     * @return The section of the door.
     */
    Section getSection();
}

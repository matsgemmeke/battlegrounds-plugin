package com.matsg.battlegrounds.api.game;

public interface ComponentFactory<T extends ArenaComponent> {

    /**
     * Makes a new gamemode specific component based of the given class.
     *
     * @param id the component id
     * @return a new gamemode specific component
     */
    T make(int id);
}

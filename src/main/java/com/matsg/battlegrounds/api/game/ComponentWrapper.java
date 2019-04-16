package com.matsg.battlegrounds.api.game;

public interface ComponentWrapper {

    /**
     * Searches for a component in the wrapper instance with a certain id.
     *
     * @param id The component id.
     * @return The component with the corresponding id or null if there are no matching components.
     */
    ArenaComponent getComponent(int id);

    /**
     * Removes a component from the wrapper instance.
     *
     * @param component The component to be removed.
     * @return Whether the arena was removed.
     */
    boolean removeComponent(ArenaComponent component);
}

package com.matsg.battlegrounds.api.game;

import java.util.Collection;

/**
 * Represents an object that encapsulates instance types of {@link ArenaComponent}.
 */
public interface ComponentWrapper {

    /**
     * Searches for a component in the wrapper instance with a certain id.
     *
     * @param id The component id.
     * @return The component with the corresponding id or null if there are no matching components.
     */
    ArenaComponent getComponent(int id);

    /**
     * Gets the amount of components in the wrapper instance.
     *
     * @return the component count
     */
    int getComponentCount();

    /**
     * Gets all components that the wrapper has. This collection is immutable. If a component needs to be added to the
     * section then it must use its specific container.
     *
     * @return All container components.
     */
    Collection<ArenaComponent> getComponents();

    /**
     * Removes a component from the wrapper instance.
     *
     * @param component The component to be removed.
     * @return Whether the component was removed.
     */
    boolean removeComponent(ArenaComponent component);
}

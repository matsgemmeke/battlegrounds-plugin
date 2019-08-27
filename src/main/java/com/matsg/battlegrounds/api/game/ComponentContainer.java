package com.matsg.battlegrounds.api.game;

import java.util.Collection;

/**
 * Represents an object that encapsulates a certain type of {@link ArenaComponent}.
 *
 * @param <T> the type of {@link ArenaComponent}
 */
public interface ComponentContainer<T extends ArenaComponent> {

    /**
     * Adds a component to the container.
     *
     * @param component the component to be added
     */
    void add(T component);

    /**
     * Gets a component from the section with a certain id.
     *
     * @param id the component id
     * @return the component with the corresponding id or null if there are no matching components.
     */
    T get(int id);

    /**
     * Gets all components that the container has. The returned collection is immutable and should only be used for
     * iterations. to add and remove components the methods {@link #add(ArenaComponent)} and {@link #remove(int)}
     * should be used
     *
     * @return all container components
     */
    Collection<T> getAll();

    /**
     * Removes a component from the container.
     *
     * @param id the id of the component to be removed
     */
    void remove(int id);
}

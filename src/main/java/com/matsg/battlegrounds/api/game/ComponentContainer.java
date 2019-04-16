package com.matsg.battlegrounds.api.game;

public interface ComponentContainer<T extends ArenaComponent> {

    /**
     * Adds a component to the container.
     *
     * @param component The component to be added.
     */
    void add(T component);

    /**
     * Gets a component from the section with a certain id.
     *
     * @param id The component id.
     * @return The component with the corresponding id or null if there are no matching components.
     */
    T get(int id);

    /**
     * Gets all components that the container has. The returned collection is immutable and should only be used for
     * iterations. To add and remove components the methods {@link #add(ArenaComponent)} and {@link #remove(int)}
     * should be used.
     *
     * @return All container components.
     */
    Iterable<T> getAll();

    /**
     * Removes a component from the container.
     *
     * @param id The id of the component to be removed.
     */
    void remove(int id);
}

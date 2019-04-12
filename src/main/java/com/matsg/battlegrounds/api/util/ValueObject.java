package com.matsg.battlegrounds.api.util;

/**
 * Represents an immutable value of an object.
 *
 * @param <T> The object type.
 */
public interface ValueObject<T> extends Cloneable {

    /**
     * Gets the value of the value object.
     *
     * @return The value.
     */
    T getValue();

    /**
     * Makes a copy of the value object.
     *
     * @return A new instance of the value object.
     */
    ValueObject<T> copy();
}

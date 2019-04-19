package com.matsg.battlegrounds.api.game;

/**
 * Represents a component that has a price and can be used by players in exchange for points.
 */
public interface Priceable {

    /**
     * Gets the price of the component.
     *
     * @return The component price.
     */
    int getPrice();

    /**
     * Sets the price of the component.
     *
     * @param price The component price.
     */
    void setPrice(int price);
}

package com.matsg.battlegrounds.api.entity;

/**
 * Represents an effect that modifies certain attributes of a player.
 */
public interface PlayerEffect {

    /**
     * Gets the player of which the effect is bound to.
     *
     * @return the player
     */
    GamePlayer getGamePlayer();

    /**
     * Sets the player of which the effect is bound to.
     *
     * @param gamePlayer the player
     */
    void setGamePlayer(GamePlayer gamePlayer);

    /**
     * Applies the effect to the player.
     */
    void apply();

    /**
     * Refreshes the effect.
     */
    void refresh();

    /**
     * Removes the effect from the player.
     */
    void remove();
}

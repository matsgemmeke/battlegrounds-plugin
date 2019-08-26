package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.api.entity.GamePlayer;

public interface MysteryBoxState {

    /**
     * Gets whether a mystery box is being used is its state.
     *
     * @return whether the mystery box is in use
     */
    boolean isInUse();

    /**
     * Handles logic of a mystery box interaction.
     *
     * @param gamePlayer the player who is using the mystery box
     * @return whether the interaction was accepted
     */
    boolean handleInteraction(GamePlayer gamePlayer);

    /**
     * Handles logic of a mystery box look interaction.
     *
     * @param gamePlayer the player who is looking at the mystery box
     * @return whether the look interaction was accepted
     */
    boolean handleLookInteraction(GamePlayer gamePlayer);

    /**
     * Initiates the mystery box state.
     */
    void initState();

    /**
     * Removes all requisites of the mystery box state.
     */
    void remove();
}

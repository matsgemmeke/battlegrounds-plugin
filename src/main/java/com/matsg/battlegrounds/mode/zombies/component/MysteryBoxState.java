package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.api.entity.GamePlayer;

public interface MysteryBoxState {

    /**
     * Handles logic of a mystery box interaction.
     *
     * @param gamePlayer the player who is using the mystery box
     */
    void handleInteraction(GamePlayer gamePlayer);
}

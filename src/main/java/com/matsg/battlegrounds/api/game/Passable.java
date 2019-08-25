package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.Location;

/**
 * Represents a component that reacts to players travelling through it.
 */
public interface Passable {

    /**
     * Initiates a pass interaction with a component.
     *
     * @param gamePlayer the player who passed the component
     * @param from the location the player travelled from
     * @param to the location the player travelled to
     * @return whether the pass interaction was accepted
     */
    boolean onPass(GamePlayer gamePlayer, Location from, Location to);
}

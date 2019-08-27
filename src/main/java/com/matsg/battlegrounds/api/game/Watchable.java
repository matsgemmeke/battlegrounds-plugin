package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.block.Block;

/**
 * Represents a component that is capable of reacting to players looking at it.
 */
public interface Watchable {

    /**
     * Initiates a look interaction with a component.
     *
     * @param gamePlayer the player who looks at the component
     * @param block the block which was looked at
     * @return true if the look interaction was accepted, otherwise false
     */
    boolean onLook(GamePlayer gamePlayer, Block block);
}

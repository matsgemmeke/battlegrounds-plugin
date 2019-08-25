package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.block.Block;

/**
 * Represents a component that can be constructed by a player.
 */
public interface Constructable {

    /**
     * Initiates a construction interaction with a component.
     *
     * @param gamePlayer the player who constructs the component
     * @param block the block used for construction
     * @return whether the construction was accepted or not
     */
    boolean onConstruct(GamePlayer gamePlayer, Block block);
}

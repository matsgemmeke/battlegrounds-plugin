package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.block.Block;

public interface Watchable {

    /**
     * Initiates a look interaction with a component.
     *
     * @param gamePlayer The player who looks at the component.
     * @param block The block which was looked at.
     * @return True if the look interaction was accepted, otherwise false.
     */
    boolean onLook(GamePlayer gamePlayer, Block block);
}

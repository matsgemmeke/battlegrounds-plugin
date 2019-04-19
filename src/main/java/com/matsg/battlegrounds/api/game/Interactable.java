package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.block.Block;

/**
 * Represents a component that be interacted with.
 */
public interface Interactable {

    /**
     * Initiates an interaction with a component.
     *
     * @param gamePlayer The player who interacts with the component.
     * @param block The block which was used for the interaction.
     * @return True if the interaction was accepted, otherwise false.
     */
    boolean onInteract(GamePlayer gamePlayer, Block block);
}

package com.matsg.battlegrounds.api.game;

import org.bukkit.block.Block;

public interface Obstacle extends Extent {

    Block[] getBlocks();

    boolean isClosed();

    boolean isOpen();
}

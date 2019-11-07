package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.Extent;
import org.bukkit.block.Block;

public interface Obstacle extends Extent {

    Block[] getBlocks();

    boolean isClosed();

    boolean isOpen();
}

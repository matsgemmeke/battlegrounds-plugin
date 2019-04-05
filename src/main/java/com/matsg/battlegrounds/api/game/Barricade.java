package com.matsg.battlegrounds.api.game;

import org.bukkit.Material;
import org.bukkit.block.Block;

public interface Barricade extends ArenaComponent, Extent, Interactable {

    boolean breakBlock(Block block);

    void close();

    Block[] getBlocks();

    Material getMaterial();

    boolean isClosed();

    boolean isOpen();

    void open();

    boolean repairBlock(Block block);
}

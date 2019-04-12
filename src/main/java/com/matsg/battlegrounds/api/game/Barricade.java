package com.matsg.battlegrounds.api.game;

import org.bukkit.Material;
import org.bukkit.block.Block;

public interface Barricade extends ArenaComponent, Extent, Interactable {

    Block[] getBlocks();

    Material getMaterial();

    boolean isClosed();

    boolean isOpen();

    boolean breakBlock(Block block);

    void close();

    void open();

    boolean repairBlock(Block block);
}

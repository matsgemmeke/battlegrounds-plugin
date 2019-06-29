package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.Mob;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Set;

public interface Barricade extends ArenaComponent, Extent, Interactable {

    Block[] getBlocks();

    Material getMaterial();

    Set<Mob> getMobs();

    boolean isClosed();

    boolean isOpen();

    boolean breakBlock(Block block);

    void close();

    void open();

    boolean repairBlock(Block block);
}

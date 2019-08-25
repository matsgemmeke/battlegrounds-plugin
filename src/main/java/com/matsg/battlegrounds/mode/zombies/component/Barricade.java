package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.game.*;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;

public interface Barricade extends ArenaComponent, Constructable, Extent, Lockable, Passable {

    Block[] getBlocks();

    Material getMaterial();

    List<Mob> getMobs();

    boolean isClosed();

    boolean isOpen();
}

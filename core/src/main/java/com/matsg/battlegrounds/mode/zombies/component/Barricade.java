package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.game.*;
import org.bukkit.Material;

import java.util.List;

public interface Barricade extends ArenaComponent, Constructable, Extent, Lockable, Obstacle, Passable {

    Material getMaterial();

    List<Mob> getMobs();
}

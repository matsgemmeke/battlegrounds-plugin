package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.BattleEntityType;
import org.bukkit.Location;

public interface MobSpawn extends ArenaComponent, Lockable {

    /**
     * Gets the barricade of the monster spawn. Can be null.
     *
     * @return The barricade of the monster spawn or null if it does not have one.
     */
    Barricade getBarricade();

    /**
     * Sets the barricade of the monster spawn.
     *
     * @param barricade The barricade.
     */
    void setBarricade(Barricade barricade);

    /**
     * Gets the spawn location of certain mob types.
     *
     * @param entityType The entity type to gets its spawn location of.
     * @return The spawn location of the specified mob type.
     */
    Location getSpawnLocation(BattleEntityType entityType);
}

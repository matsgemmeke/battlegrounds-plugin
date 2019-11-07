package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.Mob;
import org.bukkit.entity.Entity;

import java.util.List;

public interface MobManager {

    /**
     * Gets all mobs.
     *
     * @return the list of mobs
     */
    List<Mob> getMobs();

    /**
     * Gets the Mob instance of an entity.
     *
     * @param entity the entity
     * @return the corresponding Mob instance or null if the entity is not in the game
     */
    Mob findMob(Entity entity);

    /**
     * Gets the health bar of a mob.
     *
     * @param mob the mob
     * @return the health bar of the mob
     */
    String getHealthBar(Mob mob);
}

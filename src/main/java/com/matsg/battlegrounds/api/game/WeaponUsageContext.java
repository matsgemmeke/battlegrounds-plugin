package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.BattlegroundsEntity;
import org.bukkit.Location;

public interface WeaponUsageContext {

    /**
     * Gets nearby entities that can be hit by a player's weapon in the context.
     *
     * @param location the location of the player
     * @param team the team of the player
     * @param range the range to find entities in
     * @return the nearby entities in the context
     */
    BattlegroundsEntity[] getNearbyEntities(Location location, Team team, double range);
}

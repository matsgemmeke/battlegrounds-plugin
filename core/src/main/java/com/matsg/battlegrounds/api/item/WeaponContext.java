package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.entity.BattleEntityType;
import com.matsg.battlegrounds.api.game.Team;
import org.bukkit.Location;

public interface WeaponContext {

    /**
     * Gets nearby entities that are hostile towards a certain team.
     *
     * @param location the location of the player
     * @param team the team of the player
     * @param range the range to find entities in
     * @return the nearby entities in the context
     */
    BattleEntity[] getNearbyEnemies(Location location, Team team, double range);

    /**
     * Gets nearby entities that are present in the context.
     *
     * @param location the location
     * @param range the maximum location range to find entities in
     * @return the nearby entities in the context
     */
    BattleEntity[] getNearbyEntities(Location location, double range);

    /**
     * Gets whether a blood effect is displayed on a certain entity type in the context.
     *
     * @param entityType the entity type
     * @return whether a blood effect should be displayed
     */
    boolean hasBloodEffectDisplay(BattleEntityType entityType);
}

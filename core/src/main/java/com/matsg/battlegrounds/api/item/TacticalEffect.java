package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.item.WeaponMechanism;
import org.bukkit.Location;

public interface TacticalEffect extends WeaponMechanism<Tactical> {

    /**
     * Applies to effect to nearby entities.
     *
     * @param location the location from where the effect originates
     */
    void applyEffect(Location location);

    /**
     * Ignites a dropped item of the tactical.
     *
     * @param item the dropped item to ignite
     */
    void igniteItem(org.bukkit.entity.Item item);
}

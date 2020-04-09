package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.api.item.Tactical;
import org.bukkit.entity.Item;

public interface TacticalEffect extends WeaponMechanism<Tactical> {

    /**
     * Ignites an item entity and applies the effect to nearby entities.
     *
     * @param item the dropped item
     */
    void applyEffect(Item item);
}

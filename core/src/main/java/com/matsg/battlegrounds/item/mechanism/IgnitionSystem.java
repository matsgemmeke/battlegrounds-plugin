package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.api.item.Equipment;
import org.bukkit.entity.Item;

public interface IgnitionSystem extends WeaponMechanism<Equipment> {

    void igniteItem(Item item);
}

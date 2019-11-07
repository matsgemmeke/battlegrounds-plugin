package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.api.item.Launcher;
import com.matsg.battlegrounds.item.WeaponMechanism;
import org.bukkit.Location;

public interface LaunchSystem extends WeaponMechanism<Launcher> {

    void launch(Location direction);
}

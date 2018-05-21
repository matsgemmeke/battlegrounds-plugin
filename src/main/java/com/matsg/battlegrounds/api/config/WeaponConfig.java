package com.matsg.battlegrounds.api.config;

import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.item.WeaponType;

import java.util.Collection;

public interface WeaponConfig<T extends Weapon> extends Yaml {

    /**
     * Gets the weapon with a certain name. Returns null if it doesn't exists.
     *
     * @param name The name of the weapon
     * @return The weapon with the corresponding name
     */
    T get(String name);

    /**
     * Gets the list which contains all weapons
     *
     * @return The whole weapon list
     */
    Collection<T> getList();

    /**
     * Gets the list which contains all weapons of this type
     *
     * @param weaponType The weapon type filter
     * @return The whole weapon list only containing
     */
    Collection<T> getList(WeaponType weaponType);
}
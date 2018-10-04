package com.matsg.battlegrounds.api.config;

import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.WeaponType;

import java.util.Collection;

public interface ItemConfig<T extends Item> extends Yaml {

    /**
     * Gets the item with a certain id or name. Returns null if it doesn't exists.
     *
     * @param name The id or name of the item
     * @return The item with the corresponding id or name
     */
    T get(String name);

    /**
     * Gets the list which contains all items
     *
     * @return The entire item list
     */
    Collection<T> getList();

    /**
     * Gets the list which contains all items of this type
     *
     * @param weaponType The weapon type filter
     * @return The whole weapon list only containing
     */
    Collection<T> getList(WeaponType weaponType);
}
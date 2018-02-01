package com.matsg.battlegrounds.api.config;

import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.item.WeaponType;

public interface WeaponConfig<T extends Weapon> extends Yaml {

    T get(String name);

    Iterable<T> getList();

    Iterable<T> getList(WeaponType weaponType);
}
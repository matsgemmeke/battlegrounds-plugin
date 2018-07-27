package com.matsg.battlegrounds.config;

import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.item.WeaponType;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.List;

abstract class WeaponSerializer<T extends Weapon> {

    private List<WeaponType> types;

    WeaponSerializer(WeaponType... types) {
        this.types = Arrays.asList(types);
    }

    boolean hasType(WeaponType type) {
        return types.contains(type);
    }

    abstract T getFromSection(ConfigurationSection section) throws ItemFormatException;
}
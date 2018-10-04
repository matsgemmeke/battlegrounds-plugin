package com.matsg.battlegrounds.config;

import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.WeaponType;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.List;

public abstract class ItemSerializer<T extends Item> {

    private List<WeaponType> types;

    ItemSerializer(WeaponType... types) {
        this.types = Arrays.asList(types);
    }

    boolean hasType(WeaponType type) {
        return types.contains(type);
    }

    abstract T getFromSection(ConfigurationSection section) throws ItemFormatException;
}
package com.matsg.battlegrounds.config;

import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.ItemType;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.List;

public abstract class ItemSerializer<T extends Item> {

    private List<ItemType> types;

    ItemSerializer(ItemType... types) {
        this.types = Arrays.asList(types);
    }

    boolean hasType(ItemType type) {
        return types.contains(type);
    }

    abstract T getFromSection(ConfigurationSection section) throws ItemFormatException;
}
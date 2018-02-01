package com.matsg.battlegrounds.api.game;

import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public interface ItemRegistry {

    void addItem(Item item);

    void clear();

    Item getItem(ItemStack itemStack);

    void removeItem(Item item);
}
package com.matsg.battlegrounds.api.item;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public interface Droppable extends DropListener {

    Collection<Item> getDroppedItems();

    boolean isRelated(ItemStack itemStack);

    boolean onPickUp(Player player, Item item);
}
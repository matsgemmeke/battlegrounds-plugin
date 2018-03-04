package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public interface ItemRegistry {

    void addItem(Item item);

    void clear();

    Item getItem(ItemStack itemStack);

    Item getItemIgnoreMetadata(ItemStack itemStack);

    Weapon getWeapon(Player player, ItemStack itemStack);

    Weapon getWeaponIgnoreMetadata(Player player, ItemStack itemStack);

    void interact(Item item, Action action);

    void removeItem(Item item);
}
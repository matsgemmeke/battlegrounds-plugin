package com.matsg.battlegrounds.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ClickableItem {

    ItemStack getItemStack();

    void onLeftClick(Player player);

    void onRightClick(Player player);
}

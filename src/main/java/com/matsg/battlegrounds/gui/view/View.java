package com.matsg.battlegrounds.gui.view;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface View extends InventoryHolder {

    void onClick(Player player, ItemStack itemStack, ClickType clickType);

    void onClose(Player player);
}

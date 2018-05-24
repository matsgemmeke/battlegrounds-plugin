package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EditLoadoutView implements View {

    private Inventory inventory;
    private Loadout loadout;

    public EditLoadoutView(Battlegrounds plugin, Loadout loadout) {
        this.inventory = plugin.getServer().createInventory(this, 27, EnumMessage.VIEW_EDIT_LOADOUT.getMessage(
                new Placeholder("bg_loadout", loadout.getName())
        ));
        this.loadout = loadout;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {

    }

    public boolean onClose() {
        return false;
    }
}
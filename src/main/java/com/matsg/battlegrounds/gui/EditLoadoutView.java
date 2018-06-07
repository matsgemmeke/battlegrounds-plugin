package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class EditLoadoutView implements View {

    private Inventory inventory;
    private Loadout loadout;

    public EditLoadoutView(Battlegrounds plugin, Loadout loadout) {
        this.inventory = buildInventory(plugin.getServer().createInventory(this, 27, EnumMessage.TITLE_EDIT_LOADOUT.getMessage(
                new Placeholder("bg_loadout", loadout.getName())
        )));
        this.loadout = loadout;
    }

    public Inventory getInventory() {
        return inventory;
    }

    private Inventory buildInventory(Inventory inventory) {
        int i = 0;
        for (Weapon weapon : loadout.getWeapons()) {
            inventory.setItem((i += 2) + 8, new ItemStackBuilder(getType(weapon))
                    .addItemFlags(ItemFlag.values())
                    .setDisplayName()
                    .setLore(ChatColor.GRAY + getName(weapon)));
        }
        return inventory;
    }

    private String getName(Weapon weapon) {
        return weapon != null ? weapon.getName() : EnumMessage.NONE_SELECTED.getMessage();
    }

    private Material getType(Weapon weapon) {
        return weapon != null ? weapon.getItemStack().getType() : Material.BARRIER;
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {

    }

    public boolean onClose() {
        return false;
    }
}
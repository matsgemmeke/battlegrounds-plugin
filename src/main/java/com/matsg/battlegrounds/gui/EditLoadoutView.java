package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.api.Battlegrounds;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditLoadoutView implements View {

    private Battlegrounds plugin;
    private Inventory inventory;
    private Loadout loadout;
    private Map<ItemStack, Weapon> weapons;

    public EditLoadoutView(Battlegrounds plugin, Loadout loadout) {
        this.loadout = loadout;
        this.plugin = plugin;
        this.weapons = new HashMap<>();

        this.inventory = buildInventory(plugin.getServer().createInventory(this, 27, EnumMessage.TITLE_EDIT_LOADOUT.getMessage(
                new Placeholder("bg_loadout", loadout.getName())
        )));

        inventory.setItem(26, new ItemStackBuilder(new ItemStack(Material.COMPASS)).setDisplayName(EnumMessage.GO_BACK.getMessage()).build());
    }

    public Inventory getInventory() {
        return inventory;
    }

    private Inventory buildInventory(Inventory inventory) {
        int i = -2;
        for (Weapon weapon : loadout.getWeapons()) {
            ItemStack itemStack = new ItemStackBuilder(getItemStack(weapon))
                    .addItemFlags(ItemFlag.values())
                    .setDisplayName(ChatColor.WHITE + getDisplayName(weapon))
                    .setLore(ChatColor.WHITE + getName(weapon), EnumMessage.EDIT_WEAPON.getMessage())
                    .setUnbreakable(true)
                    .build();

            inventory.setItem(i += 2, itemStack);
            weapons.put(itemStack, weapon);
        }
        return inventory;
    }

    private String getDisplayName(Weapon weapon) {
        if (weapon == loadout.getPrimary()) {
            return EnumMessage.WEAPON_PRIMARY.getMessage();
        } else if (weapon == loadout.getSecondary()) {
            return EnumMessage.WEAPON_SECONDARY.getMessage();
        } else if (weapon == loadout.getEquipment()) {
            return EnumMessage.WEAPON_EQUIPMENT.getMessage();
        } else if (weapon == loadout.getKnife()) {
            return EnumMessage.WEAPON_KNIFE.getMessage();
        }
        return null;
    }

    private ItemStack getItemStack(Weapon weapon) {
        return weapon != null ? weapon.getItemStack() : new ItemStack(Material.BARRIER);
    }

    private String getName(Weapon weapon) {
        return weapon != null ? weapon.getName() : EnumMessage.NONE_SELECTED.getMessage();
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }
        if (itemStack.getType() == Material.COMPASS) {
            player.openInventory(new LoadoutManagerView(plugin, player).getInventory());
            return;
        }
        Weapon weapon = weapons.get(itemStack);
        if (weapon == null) {
            return;
        }
        if (weapon.getType().hasSubTypes()) {
            player.openInventory(new WeaponsView(plugin, loadout, weapon.getType().getDefaultItemSlot(), this).getInventory());
        } else {
            List<Weapon> weapons = new ArrayList<>();
            weapons.addAll(plugin.getKnifeConfig().getList());
            player.openInventory(new SelectWeaponView(plugin, player, loadout, weapon.getType(), weapons, inventory).getInventory());
        }
    }

    public boolean onClose() {
        return true;
    }
}
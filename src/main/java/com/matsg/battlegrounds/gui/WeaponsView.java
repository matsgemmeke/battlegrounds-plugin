package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.item.EquipmentType;
import com.matsg.battlegrounds.item.FirearmType;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WeaponsView implements View {

    private Battlegrounds plugin;
    private Inventory inventory, previous;
    private Loadout loadout;
    private List<Weapon> weapons;

    public WeaponsView(Battlegrounds plugin, Loadout loadout, ItemSlot itemSlot) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 27, Message.create(TranslationKey.VIEW_WEAPONS));
        this.loadout = loadout;
        this.weapons = getWeaponList(plugin);

        // Add all firearm class types
        for (FirearmType firearmType : FirearmType.values()) {
            List<Firearm> list = (List) plugin.getFirearmConfig().getList(firearmType);
            if (list.size() > 0) {
                Firearm firearm = list.get(0);
                if (firearm.getType().getDefaultItemSlot() == itemSlot) {
                    addWeapon(firearm);
                }
            }
        }
        // Add all explosive class types
        for (EquipmentType equipmentType : EquipmentType.values()) {
            List<Equipment> list = (List) plugin.getEquipmentConfig().getList(equipmentType);
            if (list.size() > 0) {
                Equipment equipment = list.get(0);
                if (equipment.getType().getDefaultItemSlot() == itemSlot) {
                    addWeapon(equipment);
                }
            }
        }
        // Add a knife
        List<Knife> list = (List) plugin.getKnifeConfig().getList();
        if (list.size() > 0) {
            Knife knife = list.get(0);
            if (knife.getType().getDefaultItemSlot() == itemSlot) {
                addWeapon(knife);
            }
        }

        inventory.setItem(26, new ItemStackBuilder(new ItemStack(Material.COMPASS)).setDisplayName(Message.create(TranslationKey.GO_BACK)).build());
    }

    public WeaponsView(Battlegrounds plugin, Loadout loadout, ItemSlot itemSlot, Inventory previous) {
        this(plugin, loadout, itemSlot);
        this.previous = previous;
    }

    private void addWeapon(Weapon weapon) {
        inventory.addItem(new ItemStackBuilder(weapon.getItemStack().clone())
                .addItemFlags(ItemFlag.values())
                .setDisplayName(ChatColor.WHITE + weapon.getType().getName())
                .setLore(new String[0])
                .setUnbreakable(true)
                .build());
    }

    private static List<Weapon> getWeaponList(Battlegrounds plugin) {
        List<Weapon> list = new ArrayList<>();
        list.addAll(plugin.getEquipmentConfig().getList());
        list.addAll(plugin.getFirearmConfig().getList());
        list.addAll(plugin.getKnifeConfig().getList());
        return list;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }
        if (itemStack.getType() == Material.COMPASS && previous != null) {
            player.openInventory(previous);
            return;
        }

        List<Weapon> weaponList = new ArrayList<>();
        Weapon weapon = null;

        for (Weapon w : weapons) {
            if (w.getItemStack().getDurability() == itemStack.getDurability() && w.getItemStack().getType() == itemStack.getType()) {
                weapon = w;
                break;
            }
        }

        if (weapon instanceof Equipment) {
            Equipment equipment = (Equipment) weapon;
            weaponList.addAll(plugin.getEquipmentConfig().getList(equipment.getType()));
            player.openInventory(new SelectWeaponView(plugin, player, loadout, equipment.getType(), weaponList, inventory).getInventory());
        }
        if (weapon instanceof Firearm) {
            Firearm firearm = (Firearm) weapon;
            weaponList.addAll(plugin.getFirearmConfig().getList(firearm.getType()));
            player.openInventory(new SelectWeaponView(plugin, player, loadout, firearm.getType(), weaponList, inventory).getInventory());
        }
        if (weapon instanceof Knife) {
            weaponList.addAll(plugin.getKnifeConfig().getList());
            player.openInventory(new SelectWeaponView(plugin, player, loadout, weapon.getType(), weaponList, inventory).getInventory());
        }
    }

    public boolean onClose() {
        return true;
    }
}
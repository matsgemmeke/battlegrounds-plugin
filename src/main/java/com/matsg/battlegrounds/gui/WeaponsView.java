package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.Equipment;
import com.matsg.battlegrounds.api.item.FireArm;
import com.matsg.battlegrounds.api.item.Knife;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.item.EquipmentType;
import com.matsg.battlegrounds.item.FireArmType;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.ItemStackBuilder;
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
    private Inventory inventory;
    private List<Weapon> weapons;

    public WeaponsView(Battlegrounds plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 27, EnumMessage.TITLE_WEAPONS.getMessage());
        this.weapons = getWeaponList(plugin);

        // Add all firearm class types
        for (FireArmType fireArmType : FireArmType.values()) {
            List<FireArm> list = (List) plugin.getFireArmConfig().getList(fireArmType);
            if (list.size() > 0) {
                addWeapon(list.get(0));
            }
        }
        // Add all explosive class types
        for (EquipmentType equipmentType : EquipmentType.values()) {
            List<Equipment> list = (List) plugin.getEquipmentConfig().getList(equipmentType);
            if (list.size() > 0) {
                addWeapon(list.get(0));
            }
        }
        // Add a knife
        List<Knife> list = (List) plugin.getKnifeConfig().getList();
        if (list.size() > 0) {
            addWeapon(list.get(0));
        }
    }

    private void addWeapon(Weapon weapon) {
        inventory.addItem(new ItemStackBuilder(weapon.getItemStack().clone())
                .addItemFlags(ItemFlag.values())
                .setDisplayName("§f" + weapon.getType().getName())
                .setUnbreakable(true)
                .build());
    }


    private static List<Weapon> getWeaponList(Battlegrounds plugin) {
        List<Weapon> list = new ArrayList<>();
        list.addAll(plugin.getEquipmentConfig().getList());
        list.addAll(plugin.getFireArmConfig().getList());
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
            player.openInventory(new WeaponTypeView(plugin, equipment.getType().getName(), weaponList, this).getInventory());
        }
        if (weapon instanceof FireArm) {
            FireArm fireArm = (FireArm) weapon;
            weaponList.addAll(plugin.getFireArmConfig().getList(fireArm.getType()));
            player.openInventory(new WeaponTypeView(plugin, fireArm.getType().getName(), weaponList, this).getInventory());
        }
        if (weapon instanceof Knife) {
            weaponList.addAll(plugin.getKnifeConfig().getList());
            player.openInventory(new WeaponTypeView(plugin, EnumMessage.TYPE_KNIFE.getMessage(), weaponList, this).getInventory());
        }
    }

    public boolean onClose() {
        return true;
    }
}
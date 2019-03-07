package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.ItemConfig;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.config.EquipmentConfig;
import com.matsg.battlegrounds.config.FirearmConfig;
import com.matsg.battlegrounds.config.KnifeConfig;
import com.matsg.battlegrounds.item.EquipmentType;
import com.matsg.battlegrounds.item.FirearmType;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeaponsView implements View {

    private Battlegrounds plugin;
    private Inventory inventory, previous;
    private ItemConfig equipmentConfig, firearmConfig, knifeConfig;
    private ItemSlot itemSlot;
    private Loadout loadout;
    private List<Weapon> weapons;
    private MessageHelper messageHelper;

    public WeaponsView(Battlegrounds plugin, Loadout loadout, ItemSlot itemSlot) {
        this.plugin = plugin;
        this.loadout = loadout;
        this.itemSlot = itemSlot;
        this.messageHelper = new MessageHelper();

        try {
            this.equipmentConfig = new EquipmentConfig(plugin);
            this.firearmConfig = new FirearmConfig(plugin);
            this.knifeConfig = new KnifeConfig(plugin);
        } catch (IOException e) {
            return;
        }

        this.weapons = getWeaponList();
        this.inventory = prepareInventory();
    }

    public WeaponsView(Battlegrounds plugin, Loadout loadout, ItemSlot itemSlot, Inventory previous) {
        this(plugin, loadout, itemSlot);
        this.previous = previous;
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

        ItemType itemType = null;
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
            for (String id : equipmentConfig.getItemList(equipment.getType().toString())) {
                weaponList.add(plugin.getEquipmentFactory().make(id));
            }
            itemType = equipment.getType();
        }
        if (weapon instanceof Firearm) {
            Firearm firearm = (Firearm) weapon;
            for (String id : firearmConfig.getItemList(firearm.getType().toString())) {
                weaponList.add(plugin.getFirearmFactory().make(id));
            }
            itemType = firearm.getType();
        }
        if (weapon instanceof Knife) {
            for (String id : knifeConfig.getItemList()) {
                weaponList.add(plugin.getKnifeFactory().make(id));
            }
            itemType = weapon.getType();
        }

        player.openInventory(new SelectWeaponView(plugin, player, loadout, itemType, weaponList, inventory).getInventory());
    }

    public boolean onClose() {
        return true;
    }

    private void addToInventory(Inventory inventory, Weapon weapon) {
        inventory.addItem(new ItemStackBuilder(weapon.getItemStack().clone())
                .addItemFlags(ItemFlag.values())
                .setDisplayName(ChatColor.WHITE + weapon.getType().getName())
                .setLore(new String[0])
                .setUnbreakable(true)
                .build()
        );
    }

    private List<Weapon> getWeaponList() {
        List<Weapon> weaponList = new ArrayList<>();

        for (String id : equipmentConfig.getItemList()) {
            weaponList.add(plugin.getEquipmentFactory().make(id));
        }
        for (String id : firearmConfig.getItemList()) {
            weaponList.add(plugin.getFirearmFactory().make(id));
        }
        for (String id : knifeConfig.getItemList()) {
            weaponList.add(plugin.getKnifeFactory().make(id));
        }

        return weaponList;
    }

    private Inventory prepareInventory() {
        Inventory inventory = plugin.getServer().createInventory(this, 27, messageHelper.create(TranslationKey.VIEW_WEAPONS));

        // Add all firearm class types
        for (FirearmType firearmType : FirearmType.values()) {
            List<String> idList = firearmConfig.getItemList(firearmType.toString());

            if (idList.size() > 0) {
                String id = idList.get(0);
                Firearm firearm = plugin.getFirearmFactory().make(id);
                if (firearm.getType().getDefaultItemSlot() == itemSlot) {
                    addToInventory(inventory, firearm);
                }
            }
        }

        // Add all explosive class types
        for (EquipmentType equipmentType : EquipmentType.values()) {
            List<String> idList = equipmentConfig.getItemList(equipmentType.toString());

            if (idList.size() > 0) {
                String id = idList.get(0);
                Equipment equipment = plugin.getEquipmentFactory().make(id);
                if (equipment.getType().getDefaultItemSlot() == itemSlot) {
                    addToInventory(inventory, equipment);
                }
            }
        }

        // Add a knife
        List<String> idList = knifeConfig.getItemList();
        if (idList.size() > 0) {
            String id = idList.get(0);
            Knife knife = plugin.getKnifeFactory().make(id);
            if (knife.getType().getDefaultItemSlot() == itemSlot) {
                addToInventory(inventory, knife);
            }
        }

        inventory.setItem(26, new ItemStackBuilder(new ItemStack(Material.COMPASS)).setDisplayName(messageHelper.create(TranslationKey.GO_BACK)).build());
        return inventory;
    }
}

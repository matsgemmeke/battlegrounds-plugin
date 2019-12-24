package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.ItemType;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.storage.AttachmentConfig;
import com.matsg.battlegrounds.storage.EquipmentConfig;
import com.matsg.battlegrounds.storage.FirearmConfig;
import com.matsg.battlegrounds.storage.MeleeWeaponConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemFinder {

    private AttachmentConfig attachmentConfig;
    private Battlegrounds plugin;
    private EquipmentConfig equipmentConfig;
    private FirearmConfig firearmConfig;
    private MeleeWeaponConfig meleeWeaponConfig;

    public ItemFinder(Battlegrounds plugin) {
        this.plugin = plugin;

        String filePath = plugin.getDataFolder().getPath() + "/items";

        try {
            this.attachmentConfig = new AttachmentConfig(filePath, plugin.getResource("attachments.yml"));
            this.equipmentConfig = new EquipmentConfig(filePath, plugin.getResource("equipment.yml"));
            this.firearmConfig = new FirearmConfig(filePath, plugin.getResource("guns.yml"));
            this.meleeWeaponConfig = new MeleeWeaponConfig(filePath, plugin.getResource("melee_weapons.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Item> findAllItems() {
        List<Item> list = new ArrayList<>();

        for (String attachmentId : attachmentConfig.getItemList()) {
            list.add(plugin.getAttachmentFactory().make(attachmentId));
        }

        list.addAll(findAllWeapons());
        return list;
    }

    public List<Weapon> findAllWeapons() {
        List<Weapon> list = new ArrayList<>();

        for (String equipmentId : equipmentConfig.getItemList()) {
            list.add(plugin.getEquipmentFactory().make(equipmentId));
        }
        for (String firearmId : firearmConfig.getItemList()) {
            list.add(plugin.getFirearmFactory().make(firearmId));
        }
        for (String meleeWeaponId : meleeWeaponConfig.getItemList()) {
            list.add(plugin.getMeleeWeaponFactory().make(meleeWeaponId));
        }

        return list;
    }

    public Item findItem(String id) {
        if (attachmentConfig.getItemList().contains(id)) {
            return plugin.getAttachmentFactory().make(id);
        }
        return findWeapon(id);
    }

    public Weapon findWeapon(String id) {
        if (equipmentConfig.getItemList().contains(id)) {
            return plugin.getEquipmentFactory().make(id);
        }
        if (firearmConfig.getItemList().contains(id)) {
            return plugin.getFirearmFactory().make(id);
        }
        if (meleeWeaponConfig.getItemList().contains(id)) {
            return plugin.getMeleeWeaponFactory().make(id);
        }
        return null;
    }
}

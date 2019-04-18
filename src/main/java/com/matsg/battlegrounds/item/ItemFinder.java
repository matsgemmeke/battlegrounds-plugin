package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.storage.AttachmentConfig;
import com.matsg.battlegrounds.storage.EquipmentConfig;
import com.matsg.battlegrounds.storage.FirearmConfig;
import com.matsg.battlegrounds.storage.MeleeWeaponConfig;

import java.io.IOException;

public class ItemFinder {

    private AttachmentConfig attachmentConfig;
    private Battlegrounds plugin;
    private EquipmentConfig equipmentConfig;
    private FirearmConfig firearmConfig;
    private MeleeWeaponConfig meleeWeaponConfig;

    public ItemFinder(Battlegrounds plugin) {
        this.plugin = plugin;

        try {
            this.attachmentConfig = new AttachmentConfig(plugin);
            this.equipmentConfig = new EquipmentConfig(plugin);
            this.firearmConfig = new FirearmConfig(plugin);
            this.meleeWeaponConfig = new MeleeWeaponConfig(plugin);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

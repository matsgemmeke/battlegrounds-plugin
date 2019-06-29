package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.*;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class BattleLoadout implements Loadout {

    private final int loadoutNr;
    private Equipment equipment;
    private Firearm primary, secondary;
    private MeleeWeapon meleeWeapon;
    private String name;

    public BattleLoadout(int loadoutNr, String name, Firearm primary, Firearm secondary, Equipment equipment, MeleeWeapon meleeWeapon) {
        this.loadoutNr = loadoutNr;
        this.name = name;
        this.primary = primary;
        this.secondary = secondary;
        this.equipment = equipment;
        this.meleeWeapon = meleeWeapon;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public int getLoadoutNr() {
        return loadoutNr;
    }

    public MeleeWeapon getMeleeWeapon() {
        return meleeWeapon;
    }

    public void setMeleeWeapon(MeleeWeapon meleeWeapon) {
        this.meleeWeapon = meleeWeapon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Firearm getPrimary() {
        return primary;
    }

    public void setPrimary(Firearm primary) {
        this.primary = primary;
    }

    public Firearm getSecondary() {
        return secondary;
    }

    public void setSecondary(Firearm secondary) {
        this.secondary = secondary;
    }

    public Loadout clone() {
        try {
            BattleLoadout loadout = (BattleLoadout) super.clone();
            if (primary != null) {
                loadout.primary = primary.clone();
            }
            if (secondary != null) {
                loadout.secondary = secondary.clone();
            }
            if (equipment != null) {
                loadout.equipment = equipment.clone();
            }
            if (meleeWeapon != null) {
                loadout.meleeWeapon = meleeWeapon.clone();
            }
            return loadout;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, String> convertToMap() {
        Map<String, String> map = new HashMap<>();
        map.put("loadout_nr", String.valueOf(loadoutNr));
        map.put("loadout_name", name);

        if (primary != null) {
            map.put("primary", primary.getId());

            if (primary instanceof Gun) {
                map.put("primary_attachments", convertAttachmentsToString((Gun) primary));
            }
        }
        if (secondary != null) {
            map.put("secondary", secondary.getId());

            if (secondary instanceof Gun) {
                map.put("secondary_attachments", convertAttachmentsToString((Gun) secondary));
            }
        }
        if (equipment != null) {
            map.put("equipment", equipment.getId());
        }
        if (meleeWeapon != null) {
            map.put("melee_weapon", meleeWeapon.getId());
        }

        return map;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Loadout)) {
            return false;
        }
        Loadout loadout = (Loadout) obj;
        return loadoutNr == loadout.getLoadoutNr() && name.equals(loadout.getName());
    }

    public Weapon getWeapon(ItemSlot itemSlot) {
        for (Weapon weapon : getWeapons()) {
            if (weapon != null && weapon.getItemSlot() == itemSlot) {
                return weapon;
            }
        }
        return null;
    }

    public Weapon getWeapon(ItemStack itemStack) {
        for (Weapon weapon : getWeapons()) {
            if (weapon != null && weapon.getItemStack().equals(itemStack)) {
                return weapon;
            }
        }
        return null;
    }

    public Weapon getWeapon(String name) {
        for (Weapon weapon : getWeapons()) {
            if (weapon != null && weapon.getName().equals(name)) {
                return weapon;
            }
        }
        return null;
    }

    public Weapon getWeaponIgnoreMetadata(ItemStack itemStack) {
        for (Weapon weapon : getWeapons()) {
            if (weapon != null) {
                ItemStack other = weapon.getItemStack();
                if (other != null && other.getAmount() == itemStack.getAmount()
                        && other.getDurability() == itemStack.getDurability() && other.getType() == itemStack.getType()) {
                    return weapon;
                }
            }
        }
        return null;
    }

    public Weapon[] getWeapons() {
        return new Weapon[] { primary, secondary, equipment, meleeWeapon };
    }

    public void removeWeapon(Weapon weapon) {
        if (primary == weapon) {
            primary = null;
        } else if (secondary == weapon) {
            secondary = null;
        } else if (equipment == weapon) {
            equipment = null;
        } else if (meleeWeapon == weapon) {
            meleeWeapon = null;
        }
    }

    public void updateInventory() {
        for (Weapon weapon : getWeapons()) {
            if (weapon != null) {
                weapon.update();
            }
        }
    }

    private String convertAttachmentsToString(Gun gun) {
        StringBuilder builder = new StringBuilder();
        for (Attachment attachment : gun.getAttachments()) {
            builder.append(attachment.getId());
            if (gun.getAttachments().size() > gun.getAttachments().indexOf(attachment) + 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }
}

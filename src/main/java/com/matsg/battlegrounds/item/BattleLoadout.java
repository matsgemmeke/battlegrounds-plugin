package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.*;
import org.bukkit.inventory.ItemStack;

public class BattleLoadout implements Loadout {

    private final int id;
    private boolean defaultLoadout;
    private Equipment equipment;
    private FireArm primary, secondary;
    private Knife knife;
    private String name;

    public BattleLoadout(int id, String name, FireArm primary, FireArm secondary, Equipment equipment, Knife knife) {
        this.id = id;
        this.name = name;
        this.primary = primary;
        this.secondary = secondary;
        this.equipment = equipment;
        this.knife = knife;
        this.defaultLoadout = false;

        for (Weapon weapon : getWeapons()) {
            if (weapon != null) {
                weapon.setItemSlot(weapon.getType().getDefaultItemSlot());
            }
        }
    }

    public BattleLoadout(int id, String name, FireArm primary, FireArm secondary, Equipment equipment, Knife knife, boolean defaultLoadout) {
        this(id, name, primary, secondary, equipment, knife);
        this.defaultLoadout = defaultLoadout;
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
            if (knife != null) {
                loadout.knife = knife.clone();
            }
            return loadout;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public int getId() {
        return id;
    }

    public Knife getKnife() {
        return knife;
    }

    public String getName() {
        return name;
    }

    public FireArm getPrimary() {
        return primary;
    }

    public FireArm getSecondary() {
        return secondary;
    }

    public boolean isDefaultLoadout() {
        return defaultLoadout;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public void setKnife(Knife knife) {
        this.knife = knife;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrimary(FireArm primary) {
        this.primary = primary;
    }

    public void setSecondary(FireArm secondary) {
        this.secondary = secondary;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Loadout)) {
            return false;
        }
        Loadout loadout = (Loadout) obj;
        return id == loadout.getId() && name.equals(loadout.getName());
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
        return new Weapon[] { primary, secondary, equipment, knife };
    }

    public void removeWeapon(Weapon weapon) {
        if (primary == weapon) {
            primary = null;
        } else if (secondary == weapon) {
            secondary = null;
        } else if (equipment == weapon) {
            equipment = null;
        } else if (knife == weapon) {
            knife = null;
        }
    }

    public void updateInventory() {
        for (Weapon weapon : getWeapons()) {
            if (weapon != null) {
                weapon.update();
            }
        }
    }
}
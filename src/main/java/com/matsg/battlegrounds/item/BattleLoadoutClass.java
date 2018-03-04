package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.*;
import org.bukkit.inventory.ItemStack;

public class BattleLoadoutClass implements LoadoutClass {

    private final String name;
    private Equipment equipment;
    private FireArm primary, secondary;
    private Knife knife;

    public BattleLoadoutClass(String name) {
        this.name = name;
    }

    public BattleLoadoutClass(String name, FireArm primary, FireArm secondary, Equipment equipment, Knife knife) {
        this.name = name;
        this.primary = primary;
        this.secondary = secondary;
        this.equipment = equipment;
        this.knife = knife;
    }

    public Equipment getEquipment() {
        return equipment;
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

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public void setKnife(Knife knife) {
        this.knife = knife;
    }

    public void setPrimary(FireArm primary) {
        this.primary = primary;
    }

    public void setSecondary(FireArm secondary) {
        this.secondary = secondary;
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
}
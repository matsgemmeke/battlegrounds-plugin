package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.*;
import org.bukkit.inventory.ItemStack;

public class BattleLoadoutClass implements LoadoutClass {

    private final String name;
    private FireArm primary, secondary;
    private Knife knife;
    private Lethal lethal;
    private Tactical tactical;

    public BattleLoadoutClass(String name) {
        this.name = name;
    }

    public BattleLoadoutClass(String name, FireArm primary, FireArm secondary, Lethal lethal, Tactical tactical, Knife knife) {
        this.name = name;
        this.primary = primary;
        this.secondary = secondary;
        this.lethal = lethal;
        this.tactical = tactical;
        this.knife = knife;
    }

    public Knife getKnife() {
        return knife;
    }

    public Lethal getLethal() {
        return lethal;
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

    public Tactical getTactical() {
        return tactical;
    }

    public void setKnife(Knife knife) {
        this.knife = knife;
    }

    public void setLethal(Lethal lethal) {
        this.lethal = lethal;
    }

    public void setPrimary(FireArm primary) {
        this.primary = primary;
    }

    public void setSecondary(FireArm secondary) {
        this.secondary = secondary;
    }

    public void setTactical(Tactical tactical) {
        this.tactical = tactical;
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
        return new Weapon[] { primary, secondary, lethal, tactical, knife };
    }
}
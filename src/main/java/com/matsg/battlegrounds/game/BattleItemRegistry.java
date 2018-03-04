package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.ItemRegistry;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class BattleItemRegistry implements ItemRegistry {

    private Set<Item> items;

    public BattleItemRegistry() {
        this.items = new HashSet<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void clear() {
        items.clear();
    }

    public Item getItem(ItemStack itemStack) {
        for (Item item : items) {
            if (item.getItemStack().equals(itemStack)) {
                return item;
            }
        }
        return null;
    }

    public Item getItemIgnoreMetadata(ItemStack itemStack) {
        for (Item item : items) {
            ItemStack other = item.getItemStack();
            if (other != null && other.getAmount() == itemStack.getAmount()
                    && other.getDurability() == itemStack.getDurability()
                    && other.getType() == itemStack.getType()) {
                return item;
            }
        }
        return null;
    }

     public Weapon getWeapon(Player player, ItemStack itemStack) {
        for (Weapon weapon : getWeapons()) {
            if (weapon.getGamePlayer() != null && weapon.getGamePlayer().getPlayer() == player
                    && weapon.getItemStack() != null && weapon.getItemStack().equals(itemStack)) {
                return weapon;
            }
        }
        return null;
     }

    public Weapon getWeaponIgnoreMetadata(Player player, ItemStack itemStack) {
        for (Weapon weapon : getWeapons()) {
            ItemStack other = weapon.getItemStack();
            if (weapon.getGamePlayer() != null && weapon.getGamePlayer().getPlayer() == player
                    && other != null && other.getAmount() == itemStack.getAmount()
                    && other.getDurability() == itemStack.getDurability()
                    && other.getType() == itemStack.getType()) {
                return weapon;
            }
        }
        return null;
    }

     private Set<Weapon> getWeapons() {
        Set<Weapon> set = new HashSet<>();
        for (Item item : items) {
            if (item instanceof Weapon) {
                set.add((Weapon) item);
            }
        }
        return set;
     }

    public void interact(Item item, Action action) {
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            item.onLeftClick();
        } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            item.onRightClick();
        }
    }

    public void removeItem(Item item) {
        items.remove(item);
    }
}
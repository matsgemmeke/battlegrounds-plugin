package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.ItemRegistry;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BattleItemRegistry implements ItemRegistry {

    private Set<Item> items;

    public BattleItemRegistry() {
        this.items = new HashSet<>();
    }

    public Set<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        if (items.contains(item)) {
            return;
        }
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
            if (other != null && other.getAmount() == itemStack.getAmount() && other.getDurability() == itemStack.getDurability() && other.getType() == itemStack.getType()) {
                return item;
            }
        }
        return null;
    }

    public Weapon getWeapon(GamePlayer gamePlayer, ItemStack itemStack) {
        for (Weapon weapon : getWeaponList()) {
            if (weapon.getGamePlayer() == gamePlayer && weapon.getItemStack().equals(itemStack)) {
                return weapon;
            }
        }
        return null;
    }

    public Weapon getWeaponIgnoreMetadata(GamePlayer gamePlayer, ItemStack itemStack) {
        for (Weapon weapon : getWeaponList()) {
            ItemStack other = weapon.getItemStack();
            if (weapon.getGamePlayer() == gamePlayer && other != null
                    && other.getAmount() == itemStack.getAmount()
                    && other.getDurability() == itemStack.getDurability()
                    && other.getType() == itemStack.getType()) {
                return weapon;
            }
        }
        return null;
    }

    private List<Weapon> getWeaponList() {
        List<Weapon> list = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof Weapon) {
                list.add((Weapon) item);
            }
        }
        return list;
    }

    public void interact(GamePlayer gamePlayer, Item item, Action action) {
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            item.onLeftClick(gamePlayer);
        } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            item.onRightClick(gamePlayer);
        }
    }

    public void removeItem(Item item) {
        items.remove(item);
    }
}

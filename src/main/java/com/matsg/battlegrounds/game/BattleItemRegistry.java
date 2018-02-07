package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.ItemRegistry;
import com.matsg.battlegrounds.api.item.Item;
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

    public Item getItem(Player player, ItemStack itemStack) {
        for (Item item : items) {
            if (item.getItemStack().equals(itemStack) && item.getGamePlayer() != null && item.getGamePlayer().getPlayer() == player) {
                return item;
            }
        }
        return null;
    }

    public Item getItemIgnoreMetadata(Player player, ItemStack itemStack) {
        for (Item item : items) {
            ItemStack other = item.getItemStack();
            if (other != null && other.getAmount() == itemStack.getAmount()
                    && other.getDurability() == itemStack.getDurability() && other.getType() == itemStack.getType()
                    && item.getGamePlayer() != null && item.getGamePlayer().getPlayer() == player) {
                return item;
            }
        }
        return null;
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
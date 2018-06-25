package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.ItemSlot;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class BattleItem implements Item {

    protected static Battlegrounds plugin = BattlegroundsPlugin.getPlugin();
    protected Game game;
    protected ItemSlot itemSlot;
    protected ItemStack itemStack;
    protected String name;

    public BattleItem(String name, ItemStack itemStack) {
        this.itemStack = itemStack;
        this.name = name;
    }

    public Item clone() {
        try {
            Item item = (Item) super.clone();
            if (itemStack != null) {
                item.setItemStack(itemStack.clone());
            }
            return item;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Game getGame() {
        return game;
    }

    public ItemSlot getItemSlot() {
        return itemSlot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getName() {
        return name;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setItemSlot(ItemSlot itemSlot) {
        this.itemSlot = itemSlot;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int compareTo(Item item) {
        return game == item.getGame() && itemStack.equals(item.getItemStack()) ? 0 : -1;
    }

    public void onDrop(Player player) { }

    public void onLeftClick(Player player) { }

    public void onPickUp(Player player, org.bukkit.entity.Item itemEntity) { }

    public void onRightClick(Player player) { }

    public void onSwitch(Player player) { }
}
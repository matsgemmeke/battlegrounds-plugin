package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.ItemSlot;
import org.bukkit.inventory.ItemStack;

public abstract class BattleItem implements Item {

    protected static Battlegrounds plugin = BattlegroundsPlugin.getPlugin();
    protected Game game;
    protected GamePlayer gamePlayer;
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

    public GamePlayer getGamePlayer() {
        return gamePlayer;
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

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public void setItemSlot(ItemSlot itemSlot) {
        this.itemSlot = itemSlot;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int compareTo(Item item) {
        return gamePlayer == item.getGamePlayer() && itemStack.equals(item.getItemStack()) && itemSlot == item.getItemSlot() ? 0 : -1;
    }
}
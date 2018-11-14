package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.ItemAttribute;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class BattleItem implements Item {

    protected static Battlegrounds plugin = BattlegroundsPlugin.getPlugin();
    protected Game game;
    protected ItemSlot itemSlot;
    protected ItemStack itemStack;
    protected List<ItemAttribute> attributes;
    protected String id, name;

    public BattleItem(String id, String name, ItemStack itemStack) {
        this.id = id;
        this.name = name;
        this.itemStack = itemStack;
        this.attributes = new ArrayList<>();
    }

    public Item clone() {
        try {
            BattleItem item = (BattleItem) super.clone();
            if (itemStack != null) {
                item.itemStack = itemStack.clone();
            }
            if (attributes != null && attributes.size() > 0) {
                List<ItemAttribute> attributes = new ArrayList<>();
                for (ItemAttribute attribute : this.attributes) {
                    attributes.add(attribute.clone());
                }
                item.attributes = attributes;
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

    public String getId() {
        return id;
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

    public ItemAttribute getAttribute(String id) {
        for (ItemAttribute attribute : attributes) {
            if (attribute.getId().equals(id)) {
                return attribute;
            }
        }
        return null;
    }

    public void onLeftClick(GamePlayer gamePlayer) { }

    public void onRightClick(GamePlayer gamePlayer) { }

    public void onSwap(GamePlayer gamePlayer) { }

    public void onSwitch(GamePlayer gamePlayer) { }

    public void remove() { }
}
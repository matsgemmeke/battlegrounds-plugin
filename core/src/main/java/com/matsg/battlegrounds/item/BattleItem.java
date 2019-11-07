package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.ItemMetadata;
import com.matsg.battlegrounds.api.util.GenericAttribute;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class BattleItem implements Item {

    protected Game game;
    protected ItemMetadata metadata;
    protected ItemSlot itemSlot;
    protected ItemStack itemStack;
    protected List<GenericAttribute> attributes;

    public BattleItem(ItemMetadata metadata, ItemStack itemStack) {
        this.metadata = metadata;
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
                List<GenericAttribute> attributes = new ArrayList<>();
                for (GenericAttribute attribute : this.attributes) {
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

    public void setGame(Game game) {
        this.game = game;
    }

    public ItemSlot getItemSlot() {
        return itemSlot;
    }

    public void setItemSlot(ItemSlot itemSlot) {
        this.itemSlot = itemSlot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ItemMetadata metadata) {
        this.metadata = metadata;
    }

    public int compareTo(Item item) {
        return game == item.getGame() && itemStack.equals(item.getItemStack()) ? 0 : -1;
    }

    public GenericAttribute getAttribute(String id) {
        for (GenericAttribute attribute : attributes) {
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

    public void setAttribute(String id, GenericAttribute attribute) {
        Iterator<GenericAttribute> iterator = attributes.iterator();

        while (iterator.hasNext()) {
            GenericAttribute other = iterator.next();
            if (other.getId().equals(id)) {
                attributes.remove(other);
                attributes.add(attribute);
                break;
            }
        }
    }
}

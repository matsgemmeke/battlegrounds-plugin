package com.matsg.battlegrounds.game.component;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.ItemChest;
import com.matsg.battlegrounds.api.item.TransactionItem;
import com.matsg.battlegrounds.util.ActionBar;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class ArenaItemChest implements ItemChest {

    private boolean locked;
    private Chest chest;
    private int id, price;
    private ItemStack itemStack;
    private TransactionItem item;

    public ArenaItemChest(int id, Chest chest, TransactionItem item, ItemStack itemStack, int price) {
        this.id = id;
        this.chest = chest;
        this.item = item;
        this.itemStack = itemStack;
        this.price = price;
    }

    public Chest getChest() {
        return chest;
    }

    public int getId() {
        return id;
    }

    public TransactionItem getItem() {
        return item;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean onInteract(GamePlayer gamePlayer, Block block) {
        // If the item chest was locked, it does not accept interactions.
        if (locked) {
            return false;
        }

        // If the player does not have enough price they can not open the item chest.
        if (gamePlayer.getPoints() < getPrice(gamePlayer)) {
            ActionBar.UNSUFFICIENT_POINTS.send(gamePlayer.getPlayer());
            return true;
        }

        return false;
    }

    public boolean onLook(GamePlayer gamePlayer, Block block) {
        // If the item chest was locked, it does not accept look interactions.
        if (locked) {
            return false;
        }

        return false;
    }

    private double getPrice(GamePlayer gamePlayer) {
        int price = this.price;

        if (gamePlayer.getLoadout().getWeaponIgnoreMetadata(itemStack) != null) {
            price /= 2;
        }

        return price;
    }
}

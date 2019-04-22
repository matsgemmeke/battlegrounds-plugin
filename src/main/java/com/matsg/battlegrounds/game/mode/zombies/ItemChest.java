package com.matsg.battlegrounds.game.mode.zombies;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.TransactionItem;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.ActionBar;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class ItemChest implements ArenaComponent, Interactable, Lockable, Priceable, Watchable {

    private boolean locked;
    private Chest chest;
    private int id, price;
    private ItemStack itemStack;
    private String itemName;
    private TransactionItem item;

    public ItemChest(int id, Chest chest, TransactionItem item, String itemName, ItemStack itemStack, int price) {
        this.id = id;
        this.chest = chest;
        this.item = item;
        this.itemName = itemName;
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

        // If the player does not have enough points they can not open the item chest.
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

        ActionBar.ITEMCHEST.send(gamePlayer.getPlayer(),
                new Placeholder("bg_item", itemName),
                new Placeholder("bg_price", getPrice(gamePlayer))
        );

        return true;
    }

    private double getPrice(GamePlayer gamePlayer) {
        int price = this.price;

        if (gamePlayer.getLoadout().getWeaponIgnoreMetadata(itemStack) != null) {
            price /= 2;
        }

        return price;
    }
}

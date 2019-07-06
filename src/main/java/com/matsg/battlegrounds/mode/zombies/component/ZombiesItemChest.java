package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.ItemType;
import com.matsg.battlegrounds.api.item.Transaction;
import com.matsg.battlegrounds.api.item.TransactionItem;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.gui.TransactionView;
import com.matsg.battlegrounds.util.ActionBar;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class ZombiesItemChest implements ItemChest {

    private boolean locked;
    private Chest chest;
    private Game game;
    private int id, price;
    private ItemStack itemStack;
    private ItemType itemType;
    private String itemName;
    private TransactionItem item;
    private Translator translator;

    public ZombiesItemChest(
            int id,
            Game game,
            Translator translator,
            Chest chest,
            TransactionItem item,
            String itemName,
            ItemStack itemStack,
            ItemType itemType,
            int price
    ) {
        this.id = id;
        this.game = game;
        this.translator = translator;
        this.chest = chest;
        this.item = item;
        this.itemName = itemName;
        this.itemStack = itemStack;
        this.itemType = itemType;
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

        int price = getPrice(gamePlayer);

        // If the player does not have enough points they can not open the item chest.
        if (gamePlayer.getPoints() < price) {
            ActionBar.UNSUFFICIENT_POINTS.send(gamePlayer.getPlayer());
            return true;
        }

        ItemSlot itemSlot = itemType.getDefaultItemSlot();

        if (itemSlot == ItemSlot.FIREARM_PRIMARY && gamePlayer.getLoadout().getSecondary() == null && gamePlayer.getPlayer().getInventory().getHeldItemSlot() == 1) {
            itemSlot = ItemSlot.FIREARM_SECONDARY;
        }

        gamePlayer.getPlayer().openInventory(new TransactionView(game, translator, item, itemStack, price, itemSlot.getSlot()) {
            public void onTransactionComplete(Transaction transaction) { }
        }.getInventory());

        return true;
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

    private int getPrice(GamePlayer gamePlayer) {
        int price = this.price;

        if (gamePlayer.getLoadout().getWeaponIgnoreMetadata(itemStack) != null) {
            price /= 2;
        }

        return price;
    }
}

package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.gui.view.TransactionView;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.util.ActionBar;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ZombiesWallWeapon implements WallWeapon {

    private boolean locked;
    private Game game;
    private int id, price;
    private ItemFrame itemFrame;
    private Translator translator;
    private Weapon weapon;

    public ZombiesWallWeapon(int id, Game game, ItemFrame itemFrame, Weapon weapon, Translator translator, int price) {
        this.id = id;
        this.game = game;
        this.itemFrame = itemFrame;
        this.weapon = weapon;
        this.translator = translator;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public ItemFrame getItemFrame() {
        return itemFrame;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Weapon getWeapon() {
        return weapon;
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

        ItemSlot itemSlot = weapon.getType().getDefaultItemSlot();

        if (itemSlot == ItemSlot.FIREARM_PRIMARY && gamePlayer.getLoadout().getSecondary() == null
                || gamePlayer.getPlayer().getInventory().getHeldItemSlot() == 1
                || game.getItemRegistry().getWeaponIgnoreMetadata(gamePlayer, weapon.getItemStack()) != null) {
            itemSlot = ItemSlot.FIREARM_SECONDARY;
        }

        ItemStack itemStack = new ItemStackBuilder(weapon.getItemStack())
                .addItemFlags(ItemFlag.values())
                .setDisplayName(ChatColor.WHITE + weapon.getMetadata().getName())
                .setLore()
                .setUnbreakable(true)
                .build();

        gamePlayer.getPlayer().openInventory(new TransactionView(game, translator, weapon, itemStack, price, itemSlot.getSlot()) {
            public void onTransactionComplete(Transaction transaction) {
                Weapon existingWeapon = gamePlayer.getLoadout().getWeapon(weapon.getMetadata().getName());
                // In case the player already has the weapon they want to buy, reset its state.
                if (existingWeapon != null) {
                    existingWeapon.resetState();
                    existingWeapon.update();
                }
                // Refresh the player effects
                gamePlayer.refreshEffects();
            }
        }.getInventory());

        return true;
    }

    public boolean onLook(GamePlayer gamePlayer, Block block) {
        // If the item chest was locked, it does not accept look interactions.
        if (locked) {
            return false;
        }

        ActionBar.ITEMCHEST.send(gamePlayer.getPlayer(),
                new Placeholder("bg_item", weapon.getMetadata().getName()),
                new Placeholder("bg_price", getPrice(gamePlayer))
        );

        return true;
    }

    private int getPrice(GamePlayer gamePlayer) {
        int price = this.price;

        if (gamePlayer.getLoadout().getWeaponIgnoreMetadata(weapon.getItemStack()) != null) {
            price /= 2;
        }

        return price;
    }
}

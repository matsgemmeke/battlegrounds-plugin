package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.gui.ViewFactory;
import com.matsg.battlegrounds.gui.view.ItemTransactionView;
import com.matsg.battlegrounds.gui.view.View;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ZombiesWallWeapon implements WallWeapon {

    private boolean locked;
    private Game game;
    private int id, price;
    private InternalsProvider internals;
    private ItemFrame itemFrame;
    private Translator translator;
    private ViewFactory viewFactory;
    private Weapon weapon;

    public ZombiesWallWeapon(
            int id,
            Game game,
            ItemFrame itemFrame,
            Weapon weapon,
            int price,
            InternalsProvider internals,
            Translator translator,
            ViewFactory viewFactory
    ) {
        this.id = id;
        this.game = game;
        this.itemFrame = itemFrame;
        this.weapon = weapon;
        this.price = price;
        this.internals = internals;
        this.translator = translator;
        this.viewFactory = viewFactory;
        this.locked = true;
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
        Player player = gamePlayer.getPlayer();

        // If the player does not have enough points they can not open the item chest.
        if (gamePlayer.getPoints() < price) {
            String actionBar = translator.translate(TranslationKey.ACTIONBAR_UNSUFFICIENT_POINTS.getPath());
            internals.sendActionBar(player, actionBar);
            return true;
        }

        ItemSlot itemSlot;

        if (weapon.getType().getDefaultItemSlot() == ItemSlot.FIREARM_PRIMARY && gamePlayer.getLoadout().getSecondary() == null
                || player.getInventory().getHeldItemSlot() == 1
                || game.getItemRegistry().getWeaponIgnoreMetadata(gamePlayer, weapon.getItemStack()) != null) {
            itemSlot = ItemSlot.FIREARM_SECONDARY;
        } else {
            itemSlot = weapon.getType().getDefaultItemSlot();
        }

        ItemStack itemStack = new ItemStackBuilder(weapon.getItemStack().clone())
                .addItemFlags(ItemFlag.values())
                .setDisplayName(ChatColor.WHITE + weapon.getMetadata().getName())
                .setLore()
                .setUnbreakable(true)
                .build();

        Consumer<Transaction> onTransactionComplete = transaction -> {
            // Subtract wall weapon price from player points
            gamePlayer.setPoints(gamePlayer.getPoints() - transaction.getPoints());
            // Find a weapon by the same name
            Weapon existingWeapon = gamePlayer.getLoadout().getWeapon(weapon.getMetadata().getName());
            // In case the player already has the weapon they want to buy, reset its state.
            if (existingWeapon != null) {
                existingWeapon.resetState();
                existingWeapon.update();
            }
            // Refresh the player effects
            gamePlayer.refreshEffects();
            // Update player score
            game.updateScoreboard();
        };

        View view = viewFactory.make(ItemTransactionView.class, instance -> {
            instance.setGame(game);
            instance.setItem(weapon);
            instance.setItemStack(itemStack);
            instance.setOnTransactionComplete(onTransactionComplete);
            instance.setPoints(price);
            instance.setSlot(itemSlot.getSlot());
        });
        view.openInventory(player);

        return true;
    }

    public boolean onLook(GamePlayer gamePlayer, Block block) {
        // If the item chest was locked, it does not accept look interactions.
        if (locked) {
            return false;
        }

        String actionBar = translator.translate(TranslationKey.ACTIONBAR_WALLWEAPON.getPath(), new Placeholder("bg_price", getPrice(gamePlayer)));
        internals.sendActionBar(gamePlayer.getPlayer(), actionBar);

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

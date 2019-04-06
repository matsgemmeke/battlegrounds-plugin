package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.Transaction;
import com.matsg.battlegrounds.api.item.TransactionItem;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class TransactionView implements View {

    private Game game;
    private int points;
    private Inventory inventory;
    private ItemSlot itemSlot;
    private ItemStack itemStack;
    private MessageHelper messageHelper;
    private TransactionItem item;

    public TransactionView(Game game, TransactionItem item, ItemSlot itemSlot, ItemStack itemStack, int points) {
        this.game = game;
        this.item = item;
        this.itemSlot = itemSlot;
        this.itemStack = itemStack;
        this.points = points;
        this.messageHelper = new MessageHelper();

        inventory = Bukkit.getServer().createInventory(this, 54, messageHelper.create(TranslationKey.TRANSACTION_VIEW_TITLE, new Placeholder("bg_slot", itemSlot.getSlot())));

        ItemStack buy = new ItemStackBuilder(new ItemStack(Material.EMERALD_BLOCK))
                .setDisplayName(messageHelper.create(TranslationKey.TRANSACTION_VIEW_BUY))
                .build();
        ItemStack discard = new ItemStackBuilder(new ItemStack(Material.REDSTONE_BLOCK))
                .setDisplayName(messageHelper.create(TranslationKey.TRANSACTION_VIEW_DISCARD))
                .build();

        inventory.setItem(4, itemStack);

        int[] buySlots = new int[] { 18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39, 45, 46, 47, 48 };
        int[] discardSlots = new int[] { 23, 24, 25, 26, 32, 33, 34, 35, 41, 42, 43, 44, 50, 51, 52, 53 };

        for (int i : buySlots) {
            inventory.setItem(i, buy);
        }
        for (int i : discardSlots) {
            inventory.setItem(i, discard);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);

        if (gamePlayer == null || itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }

        // On purchase
        if (itemStack.getType() == Material.EMERALD_BLOCK) {
            player.closeInventory();

            if (points > 0) {
                game.getPlayerManager().givePoints(gamePlayer, -points);
                game.updateScoreboard();
            }

            for (Sound sound : BattleSound.ITEM_EQUIP) {
                sound.play(game, player.getLocation());
            }

            Weapon weapon = gamePlayer.getLoadout().getWeapon(itemStack.getItemMeta().getDisplayName());

            // In case the player already has the weapon they want to buy, reset its state.
            if (weapon != null) {
                weapon.resetState();
                weapon.update();
                return;
            }

            Transaction transaction = new Transaction();
            transaction.setGamePlayer(gamePlayer);
            transaction.setItem(item);
            transaction.setItemSlot(itemSlot);
            transaction.setPoints(points);

            item.handleTransaction(transaction);

            onTransactionComplete(transaction);
        }

        // On discard
        if (itemStack.getType() == Material.REDSTONE_BLOCK) {
            player.closeInventory();
        }
    }

    public boolean onClose() {
        return false;
    }

    public abstract void onTransactionComplete(Transaction transaction);
}

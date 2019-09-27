package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.Transaction;
import com.matsg.battlegrounds.api.item.TransactionItem;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.util.BattleSound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class TransactionView implements View {

    private Game game;
    private int points;
    private int slot;
    private Inventory inventory;
    private ItemStack itemStack;
    private TransactionItem item;
    private Translator translator;

    public TransactionView(Game game, Translator translator, TransactionItem item, ItemStack itemStack, int points, int slot) {
        this.game = game;
        this.translator = translator;
        this.item = item;
        this.itemStack = itemStack;
        this.points = points;
        this.slot = slot;
        this.inventory = createInventory();
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

            Transaction transaction = new Transaction();
            transaction.setGame(game);
            transaction.setGamePlayer(gamePlayer);
            transaction.setItem(item);
            transaction.setPoints(points);
            transaction.setSlot(slot);

            item.handleTransaction(transaction);

            onTransactionComplete(transaction);
        }

        // On discard
        if (itemStack.getType() == Material.REDSTONE_BLOCK) {
            player.closeInventory();
        }
    }

    public boolean onClose() {
        return true;
    }

    public abstract void onTransactionComplete(Transaction transaction);

    private Inventory createInventory() {
        inventory = Bukkit.getServer().createInventory(this, 54, translator.translate(TranslationKey.VIEW_TRANSACTION_TITLE.getPath(), new Placeholder("bg_slot", slot + 1)));

        ItemStack buy = new ItemStackBuilder(new ItemStack(Material.EMERALD_BLOCK))
                .setDisplayName(translator.translate(TranslationKey.VIEW_TRANSACTION_BUY.getPath()))
                .build();
        ItemStack discard = new ItemStackBuilder(new ItemStack(Material.REDSTONE_BLOCK))
                .setDisplayName(translator.translate(TranslationKey.VIEW_TRANSACTION_DISCARD.getPath()))
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

        return inventory;
    }
}

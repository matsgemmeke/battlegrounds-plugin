package com.matsg.battlegrounds.gui.view;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.Transaction;
import com.matsg.battlegrounds.api.item.TransactionItem;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.util.BattleSound;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ItemTransactionView implements View {

    public Gui gui;
    private Consumer<Transaction> onTransactionComplete;
    private Game game;
    private int points;
    private int slot;
    private ItemStack itemStack;
    private TransactionItem item;
    private Translator translator;

    public ItemTransactionView setGame(Game game) {
        this.game = game;
        return this;
    }

    public ItemTransactionView setItem(TransactionItem item) {
        this.item = item;
        return this;
    }

    public ItemTransactionView setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public ItemTransactionView setOnTransactionComplete(Consumer<Transaction> onTransactionComplete) {
        this.onTransactionComplete = onTransactionComplete;
        return this;
    }

    public ItemTransactionView setPoints(int points) {
        this.points = points;
        return this;
    }

    public ItemTransactionView setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public ItemTransactionView setTranslator(Translator translator) {
        this.translator = translator;
        return this;
    }

    public void openInventory(HumanEntity entity) {
        gui.show(entity);
    }

    public void populateBuyButtons(OutlinePane pane) {
        for (int i = 0; i <= pane.getHeight(); i++) {
            for (int j = 0; j <= pane.getLength(); j++) {
                ItemStack itemStack = new ItemStackBuilder(new ItemStack(Material.EMERALD_BLOCK))
                        .setDisplayName(translator.translate(TranslationKey.VIEW_TRANSACTION_BUY.getPath()))
                        .build();

                pane.addItem(new GuiItem(itemStack, event -> {
                    HumanEntity entity = event.getWhoClicked();
                    GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer((Player) entity);

                    entity.closeInventory();

                    for (Sound sound : BattleSound.ITEM_EQUIP) {
                        sound.play(game, entity.getLocation());
                    }

                    Transaction transaction = new Transaction();
                    transaction.setGame(game);
                    transaction.setGamePlayer(gamePlayer);
                    transaction.setItem(item);
                    transaction.setPoints(points);
                    transaction.setSlot(slot);

                    item.handleTransaction(transaction);

                    onTransactionComplete.accept(transaction);
                }));
            }
        }
    }

    public void populateDiscardButtons(OutlinePane pane) {
        for (int i = 0; i <= pane.getHeight(); i++) {
            for (int j = 0; j <= pane.getLength(); j++) {
                ItemStack itemStack = new ItemStackBuilder(new ItemStack(Material.REDSTONE_BLOCK))
                        .setDisplayName(translator.translate(TranslationKey.VIEW_TRANSACTION_DISCARD.getPath()))
                        .build();

                pane.addItem(new GuiItem(itemStack, event -> {
                    event.getWhoClicked().closeInventory();
                }));
            }
        }
    }

    public void populateTransactionItem(StaticPane pane) {
        pane.addItem(new GuiItem(itemStack, event -> event.setCancelled(true)), 0, 0);
    }
}

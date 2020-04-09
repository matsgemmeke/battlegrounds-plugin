package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.gui.ViewFactory;
import com.matsg.battlegrounds.gui.view.ItemTransactionView;
import com.matsg.battlegrounds.gui.view.View;
import com.matsg.battlegrounds.mode.zombies.PerkManager;
import com.matsg.battlegrounds.mode.zombies.item.Perk;
import com.matsg.battlegrounds.api.item.Transaction;
import com.matsg.battlegrounds.api.Placeholder;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ZombiesPerkMachine implements PerkMachine {

    private static final int MAX_NUMBER_PERKS = 4;

    private boolean locked;
    private Game game;
    private int id, maxBuys, price;
    private InternalsProvider internals;
    private Map<GamePlayer, Integer> buys;
    private Perk perk;
    private PerkManager perkManager;
    private Sign sign;
    private String[] signLayout;
    private Translator translator;
    private ViewFactory viewFactory;

    public ZombiesPerkMachine(
            int id,
            Game game,
            Sign sign,
            Perk perk,
            PerkManager perkManager,
            int maxBuys,
            int price,
            InternalsProvider internals,
            Translator translator,
            ViewFactory viewFactory
    ) {
        this.id = id;
        this.game = game;
        this.sign = sign;
        this.perk = perk;
        this.perkManager = perkManager;
        this.maxBuys = maxBuys;
        this.price = price;
        this.internals = internals;
        this.translator = translator;
        this.viewFactory = viewFactory;
        this.buys = new HashMap<>();
        this.locked = true;
    }

    public int getId() {
        return id;
    }

    public int getMaxBuys() {
        return maxBuys;
    }

    public void setMaxBuys(int maxBuys) {
        this.maxBuys = maxBuys;
    }

    public Perk getPerk() {
        return perk;
    }

    public void setPerk(Perk perk) {
        this.perk = perk;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Sign getSign() {
        return sign;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public String[] getSignLayout() {
        return signLayout;
    }

    public void setSignLayout(String[] signLayout) {
        this.signLayout = signLayout;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean onInteract(GamePlayer gamePlayer, Block block) {
        // In case the perk machine is locked or the player has too many perks or the player already has bought the perk, it does not accept interactions
        if (locked || perkManager.getPerkCount(gamePlayer) >= MAX_NUMBER_PERKS || perkManager.hasPerkEffect(gamePlayer, perk.getEffect().getType())) {
            return false;
        }

        Player player = gamePlayer.getPlayer();

        // If the player does not have enough points they can not buy the perk
        if (gamePlayer.getPoints() < price) {
            String actionBar = translator.translate(TranslationKey.ACTIONBAR_UNSUFFICIENT_POINTS.getPath());
            internals.sendActionBar(player, actionBar);
            return true;
        }

        // If the player has bought the perk too many times they can not buy the perk
        if (buys.containsKey(gamePlayer) && buys.get(gamePlayer) > maxBuys) {
            String actionBar = translator.translate(TranslationKey.ACTIONBAR_PERKMACHINE_SOLD_OUT.getPath());
            internals.sendActionBar(player, actionBar);
            return true;
        }

        int slot = perkManager.getPerkCount(gamePlayer) + 4;

        Consumer<Transaction> onTransactionComplete = transaction -> {
            // Subtract perk price from player points
            gamePlayer.setPoints(gamePlayer.getPoints() - transaction.getPoints());
            // Add the perk item to the registry
            game.getItemRegistry().addItem(perk);
            // Update player score
            game.updateScoreboard();
            // Assign the player to the perk effect instance
            perk.getEffect().setGamePlayer(gamePlayer);
            // Register perk purchase to the perk manager
            perkManager.addPerk(gamePlayer, perk);
        };

        // A new perk is given out, so make a clone of the original in the perk machine
        Perk perk = this.perk.clone();

        View view = viewFactory.make(ItemTransactionView.class, instance -> {
            instance.setGame(game);
            instance.setItem(perk);
            instance.setItemStack(perk.getItemStack());
            instance.setOnTransactionComplete(onTransactionComplete);
            instance.setPoints(price);
            instance.setSlot(slot);
        });
        view.openInventory(player);

        return true;
    }

    public boolean updateSign() {
        if (signLayout == null || signLayout.length < 4) {
            return false;
        }

        for (int i = 0; i <= 3; i++) {
            sign.setLine(i, translator.createSimpleMessage(signLayout[i],
                    new Placeholder("bg_perk", perk.getEffect().getName()),
                    new Placeholder("bg_price", price)
            ));
        }

        return sign.update();
    }
}

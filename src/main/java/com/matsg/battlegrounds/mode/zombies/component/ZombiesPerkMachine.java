package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.mode.zombies.PerkManager;
import com.matsg.battlegrounds.mode.zombies.item.Perk;
import com.matsg.battlegrounds.api.item.Transaction;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.gui.TransactionView;
import com.matsg.battlegrounds.util.ActionBar;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.HashMap;
import java.util.Map;

public class ZombiesPerkMachine implements PerkMachine {

    private static final int MAX_NUMBER_PERKS = 4;

    private boolean locked;
    private Game game;
    private int id, maxBuys, price;
    private Map<GamePlayer, Integer> buys;
    private Perk perk;
    private PerkManager perkManager;
    private Sign sign;
    private String[] signLayout;
    private Translator translator;

    public ZombiesPerkMachine(int id, Game game, Sign sign, Perk perk, PerkManager perkManager, Translator translator, int maxBuys, int price) {
        this.id = id;
        this.game = game;
        this.sign = sign;
        this.perk = perk;
        this.perkManager = perkManager;
        this.translator = translator;
        this.maxBuys = maxBuys;
        this.price = price;
        this.buys = new HashMap<>();
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
        if (locked || perkManager.getPerkCount(gamePlayer) >= MAX_NUMBER_PERKS || perkManager.hasPerkEffect(gamePlayer, perk.getEffect())) {
            return false;
        }

        // If the player does not have enough points they can not buy the perk
        if (gamePlayer.getPoints() < price) {
            ActionBar.UNSUFFICIENT_POINTS.send(gamePlayer.getPlayer());
            return true;
        }

        // If the player has bought the perk too many times they can not buy the perk
        if (buys.containsKey(gamePlayer) && buys.get(gamePlayer) > maxBuys) {
            ActionBar.PERKMACHINE_SOLD_OUT.send(gamePlayer.getPlayer());
            return true;
        }

        int slot = perkManager.getPerkCount(gamePlayer) + 4;

        // A new perk is given out, so make a clone of the original in the perk machine
        Perk perk = this.perk.clone();

        gamePlayer.getPlayer().openInventory(new TransactionView(game, translator, perk, perk.getItemStack(), price, slot) {
            public void onTransactionComplete(Transaction transaction) {
                game.getItemRegistry().addItem(perk);
                perk.getEffect().setGamePlayer(gamePlayer);
                perkManager.addPerk(gamePlayer, perk);
            }
        }.getInventory());

        return false;
    }

    public boolean updateSign() {
        if (signLayout == null || signLayout.length < 4) {
            return false;
        }

        for (int i = 0; i <= 3; i ++) {
            sign.setLine(i, translator.createSimpleMessage(signLayout[i],
                    new Placeholder("bg_perk", perk.getEffect().getName()),
                    new Placeholder("bg_price", price)
            ));
        }

        return sign.update();
    }
}

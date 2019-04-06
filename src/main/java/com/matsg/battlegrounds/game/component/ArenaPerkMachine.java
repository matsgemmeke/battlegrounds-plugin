package com.matsg.battlegrounds.game.component;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PerkMachine;
import com.matsg.battlegrounds.api.item.Perk;
import com.matsg.battlegrounds.api.item.PerkEffect;
import com.matsg.battlegrounds.api.item.Transaction;
import com.matsg.battlegrounds.gui.TransactionView;
import com.matsg.battlegrounds.util.ActionBar;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.HashMap;
import java.util.Map;

public class ArenaPerkMachine implements PerkMachine {

    private static final int MAX_NUMBER_PERKS = 4;

    private boolean locked;
    private Game game;
    private int id, maxBuys, price;
    private Map<GamePlayer, Integer> buys;
    private Perk perk;
    private Sign sign;

    public ArenaPerkMachine(int id, Game game, Sign sign, Perk perk, int maxBuys) {
        this.id = id;
        this.game = game;
        this.sign = sign;
        this.perk = perk;
        this.maxBuys = maxBuys;
        this.buys = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public int getMaxBuys() {
        return maxBuys;
    }

    public Perk getPerk() {
        return perk;
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

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean onInteract(GamePlayer gamePlayer, Block block) {
        if (locked
                || gamePlayer.getPoints() < price
                || gamePlayer.getPerks().size() <= MAX_NUMBER_PERKS
                || hasPerkType(gamePlayer, perk.getEffect())) {
            return false;
        }

        if (buys.containsKey(gamePlayer) && buys.get(gamePlayer) > maxBuys) {
            ActionBar.PERKMACHINE_SOLD_OUT.send(gamePlayer.getPlayer());
            return true;
        }

        gamePlayer.getPlayer().openInventory(new TransactionView(game, perk, null, perk.getItemStack(), price) {
            public void onTransactionComplete(Transaction transaction) {
                game.getItemRegistry().addItem(perk);
                perk.setGame(game);
            }
        }.getInventory());

        return false;
    }

    private boolean hasPerkType(GamePlayer gamePlayer, PerkEffect perkEffect) {
        for (Perk perk : gamePlayer.getPerks()) {
            if (perk.getEffect() == perkEffect) {
                return true;
            }
        }
        return false;
    }
}

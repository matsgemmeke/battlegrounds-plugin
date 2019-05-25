package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.item.Perk;
import com.matsg.battlegrounds.api.item.PerkEffect;
import com.matsg.battlegrounds.api.item.Transaction;

public class BattlePerk extends BattleItem implements Perk {

    private GamePlayer gamePlayer;
    private PerkEffect effect;

    public BattlePerk(Battlegrounds plugin, PerkEffect effect) {
        super(plugin, effect.toString(), effect.getName(), effect.getItemStack());
        this.effect = effect;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public PerkEffect getEffect() {
        return effect;
    }

    public Perk clone() {
        return (Perk) super.clone();
    }

    public void handleTransaction(Transaction transaction) {
        GamePlayer gamePlayer = transaction.getGamePlayer();
        gamePlayer.getPlayer().getInventory().setItem(gamePlayer.getPerks().size() + 3, itemStack);

        effect.apply(gamePlayer);
    }

    public void remove() {
        effect.remove(gamePlayer);
    }

    public boolean update() {
        return true;
    }
}

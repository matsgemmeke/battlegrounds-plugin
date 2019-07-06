package com.matsg.battlegrounds.mode.zombies.item;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.item.BattleItem;
import com.matsg.battlegrounds.api.item.Transaction;

public class ZombiesPerk extends BattleItem implements Perk {

    private GamePlayer gamePlayer;
    private PerkEffect effect;

    public ZombiesPerk(Battlegrounds plugin, PerkEffect effect) {
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
        int slot = transaction.getSlot();

        gamePlayer.getPlayer().getInventory().setItem(slot, itemStack);

        effect.apply(gamePlayer);

        this.gamePlayer = gamePlayer;
    }

    public void remove() {
        effect.remove(gamePlayer);
    }

    public boolean update() {
        return true;
    }
}

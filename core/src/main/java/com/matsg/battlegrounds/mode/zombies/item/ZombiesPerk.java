package com.matsg.battlegrounds.mode.zombies.item;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.item.ItemMetadata;
import com.matsg.battlegrounds.item.BattleItem;
import com.matsg.battlegrounds.api.item.Transaction;
import org.bukkit.inventory.ItemStack;

public class ZombiesPerk extends BattleItem implements Perk {

    private GamePlayer gamePlayer;
    private PerkEffect effect;

    public ZombiesPerk(ItemMetadata metadata, ItemStack itemStack, PerkEffect effect) {
        super(metadata, itemStack);
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

        gamePlayer.getEffects().add(effect);
        gamePlayer.getPlayer().getInventory().setItem(slot, itemStack);

        effect.setGamePlayer(gamePlayer);
        effect.apply();

        this.gamePlayer = gamePlayer;
    }

    public void remove() {
        effect.remove();
    }

    public boolean update() {
        return true;
    }
}

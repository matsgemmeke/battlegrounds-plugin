package com.matsg.battlegrounds.mode.zombies.component.mysterybox;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.Transaction;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBox;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBoxState;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.Hologram;
import org.bukkit.entity.Item;

public class DisplayState implements MysteryBoxState {

    private static final long WEAPON_DISPLAY_DELAY = 0;
    private static final long WEAPON_DISPLAY_DURATION = 2; // The hologram changes every 2 ticks or 0.1 seconds

    private Game game;
    private GamePlayer gamePlayer;
    private Hologram hologram;
    private Item item;
    private MysteryBox mysteryBox;
    private Weapon weapon;

    public DisplayState(Game game, MysteryBox mysteryBox, GamePlayer gamePlayer, Hologram hologram, Item item, Weapon weapon) {
        this.game = game;
        this.mysteryBox = mysteryBox;
        this.gamePlayer = gamePlayer;
        this.hologram = hologram;
        this.item = item;
        this.weapon = weapon;
    }

    public boolean handleInteraction(GamePlayer gamePlayer) {
        if (this.gamePlayer != gamePlayer) {
            return true;
        }

        ItemSlot itemSlot = weapon.getType().getDefaultItemSlot();

        if (itemSlot == ItemSlot.FIREARM_PRIMARY && gamePlayer.getLoadout().getSecondary() == null
                || gamePlayer.getPlayer().getInventory().getHeldItemSlot() == 1) {
            itemSlot = ItemSlot.FIREARM_SECONDARY;
        }

        // Execute the logic of the transaction view
        for (Sound sound : BattleSound.ITEM_EQUIP) {
            sound.play(game, gamePlayer.getPlayer().getLocation());
        }

        int points = 0; // Since the player already paid to open the mystery box, the transaction is free

        Transaction transaction = new Transaction();
        transaction.setGame(game);
        transaction.setGamePlayer(gamePlayer);
        transaction.setItem(weapon);
        transaction.setPoints(points);
        transaction.setSlot(itemSlot.getSlot());

        weapon.handleTransaction(transaction);

        removeDisplay();

        mysteryBox.setState(new IdleState(game, mysteryBox));
        return true;
    }

    public void initState() {
        new BattleRunnable() {
            int time = 99;

            public void run() {
                if (!game.getState().isInProgress()) {
                    removeDisplay();
                    cancel();
                    return;
                }

                hologram.setText((double) time / 10 + "s", weapon.getName());
                hologram.update();
            }
        }.runTaskTimer(WEAPON_DISPLAY_DELAY, WEAPON_DISPLAY_DURATION);
    }

    private void removeDisplay() {
        hologram.remove();
        item.remove();
        mysteryBox.playChestAnimation(false);
    }
}

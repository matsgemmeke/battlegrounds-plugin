package com.matsg.battlegrounds.mode.zombies.component.mysterybox;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.Transaction;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBox;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBoxState;
import com.matsg.battlegrounds.util.ActionBar;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.Hologram;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class DisplayState implements MysteryBoxState {

    private static final int WEAPON_DISPLAY_DURATION = 99;
    private static final long WEAPON_DISPLAY_DELAY = 0;
    private static final long WEAPON_DISPLAY_FRAME_DURATION = 2; // The hologram changes every 2 ticks or 0.1 seconds

    private boolean inUse;
    private BukkitTask task;
    private Game game;
    private GamePlayer gamePlayer;
    private Hologram hologram;
    private Item item;
    private MysteryBox mysteryBox;
    private TaskRunner taskRunner;
    private Weapon weapon;

    public DisplayState(
            Game game,
            MysteryBox mysteryBox,
            GamePlayer gamePlayer,
            Hologram hologram,
            Item item,
            Weapon weapon,
            TaskRunner taskRunner
    ) {
        this.game = game;
        this.mysteryBox = mysteryBox;
        this.gamePlayer = gamePlayer;
        this.hologram = hologram;
        this.item = item;
        this.weapon = weapon;
        this.taskRunner = taskRunner;
        this.inUse = true;
    }

    public boolean isInUse() {
        return inUse;
    }

    public boolean handleInteraction(GamePlayer gamePlayer) {
        if (this.gamePlayer != gamePlayer) {
            return true;
        }

        ItemSlot itemSlot = weapon.getType().getDefaultItemSlot();

        if (itemSlot == ItemSlot.FIREARM_PRIMARY &&
                (gamePlayer.getLoadout().getSecondary() == null || gamePlayer.getPlayer().getInventory().getHeldItemSlot() == 1)) {
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

        remove();
        task.cancel();

        mysteryBox.setCurrentWeapon(weapon);
        mysteryBox.setState(new IdleState(game, mysteryBox, taskRunner));
        return true;
    }

    public boolean handleLookInteraction(GamePlayer gamePlayer) {
        ActionBar.MYSTERY_BOX_SWAP.send(gamePlayer.getPlayer(), new Placeholder("bg_weapon", weapon.getMetadata().getName()));
        return true;
    }

    public void initState() {
        task = taskRunner.runTaskTimer(new BukkitRunnable() {
            int time = WEAPON_DISPLAY_DURATION;

            public void run() {
                if (--time < 0 || !game.getState().isInProgress()) {
                    remove();
                    mysteryBox.setCurrentWeapon(weapon);
                    mysteryBox.setState(new IdleState(game, mysteryBox, taskRunner));
                    cancel();
                    return;
                }

                hologram.setText((double) time / 10 + "s", weapon.getMetadata().getName());
                hologram.update();
            }
        }, WEAPON_DISPLAY_DELAY, WEAPON_DISPLAY_FRAME_DURATION);
    }

    public void remove() {
        hologram.remove();
        item.remove();
        mysteryBox.playChestAnimation(false);
    }
}

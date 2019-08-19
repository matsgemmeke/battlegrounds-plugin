package com.matsg.battlegrounds.mode.zombies.component.mysterybox;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBox;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBoxState;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.Hologram;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;

import java.util.Random;

public class RotatingState implements MysteryBoxState {

    private static final long BOX_ROTATION_DELAY = 0;
    private static final long BOX_ROTATION_DURATION = 4;

    private Game game;
    private GamePlayer gamePlayer;
    private int maxRotations;
    private int rotation;
    private Item item;
    private MysteryBox mysteryBox;
    private Sound moveSound;
    private Sound rollEndSound;
    private Sound rollSound;
    private Weapon weapon;

    public RotatingState(Game game, MysteryBox mysteryBox, GamePlayer gamePlayer, int maxRotations) {
        this.game = game;
        this.mysteryBox = mysteryBox;
        this.gamePlayer = gamePlayer;
        this.maxRotations = maxRotations;
        this.moveSound = BattleSound.MYSTERY_BOX_MOVE;
        this.rollEndSound = BattleSound.MYSTERY_BOX_ROLL_END;
        this.rollSound = BattleSound.MYSTERY_BOX_ROLL;
        this.rotation = 0;
    }

    public void handleInteraction(GamePlayer gamePlayer) {
        if (++rotation >= maxRotations || this.gamePlayer != gamePlayer) {
            return;
        }

        this.gamePlayer = gamePlayer;

        int pickupDelay = 1000;

        item = mysteryBox.getLeftSide().getWorld().dropItem(getItemDropLocation(), mysteryBox.getWeapons()[0].getItemStack());
        item.setPickupDelay(pickupDelay);

        mysteryBox.playChestAnimation(true);

        new BattleRunnable() {
            public void run() {
                if (!game.getState().isInProgress()) {
                    cancel();
                    return;
                }

                if (++rotation >= maxRotations) {
                    rotateChosenWeapon();
                    cancel();
                } else {
                    rotateRandomWeapon();
                }
            }
        }.runTaskTimer(BOX_ROTATION_DELAY, BOX_ROTATION_DURATION);
    }

    private Location getItemDropLocation() {
        Block left = mysteryBox.getLeftSide();
        Block right = mysteryBox.getRightSide();

        return new Location(
                left.getWorld(),
                ((left.getX() + 0.5) + (right.getX() + 0.5)) / 2.0,
                left.getY() + 1.0,
                ((left.getZ() + 0.5) + (right.getZ() + 0.5)) / 2.0
        );
    }

    private void rotateChosenWeapon() {
        Hologram hologram = new Hologram(item.getLocation().add(0, -1, 0));

        item.setItemStack(weapon.getItemStack());

        if (!willMove()) {
            rollEndSound.play(game, item.getLocation());

            mysteryBox.setState(new DisplayState(game, mysteryBox, gamePlayer, hologram, item, weapon.getName()));
        } else {
            moveSound.play(game, item.getLocation());

            mysteryBox.setState(new MovingState());
        }
    }

    private void rotateRandomWeapon() {
        Random random = new Random();
        Weapon[] weapons = mysteryBox.getWeapons();

        do {
            weapon = weapons[random.nextInt(weapons.length)];
        } while (mysteryBox.getCurrentWeapon() == null || mysteryBox.getCurrentWeapon().equals(weapon) || gamePlayer.getLoadout().getWeapon(weapon.getName()) != null);

        item.setItemStack(weapon.getItemStack());

        rollSound.setPitch(random.nextFloat() + 0.5f);
        rollSound.play(game, item.getLocation());
    }

    private boolean willMove() {
        double random = new Random().nextDouble();
        int rolls = mysteryBox.getRolls();

        if (rolls <= 3) {
            return false; // The first three rolls will never trigger a box move
        } else if (rolls <= 7) {
            return random < 0.15;
        } else if (rolls <= 12) {
            return random < 0.3;
        }
        return random < 0.5;
    }
}

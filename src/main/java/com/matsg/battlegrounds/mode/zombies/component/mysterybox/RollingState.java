package com.matsg.battlegrounds.mode.zombies.component.mysterybox;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBox;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBoxState;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.Hologram;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class RollingState implements MysteryBoxState {

    private static final int BOX_MAX_ROTATIONS = 25;
    private static final long BOX_ROTATION_DELAY = 0;
    private static final long BOX_ROTATION_DURATION = 4;

    private boolean inUse;
    private Game game;
    private GamePlayer gamePlayer;
    private int rotation;
    private Item item;
    private MysteryBox mysteryBox;
    private Sound moveSound;
    private Sound rollEndSound;
    private Sound rollSound;
    private Weapon weapon;

    public RollingState(Game game, MysteryBox mysteryBox, GamePlayer gamePlayer) {
        this.game = game;
        this.mysteryBox = mysteryBox;
        this.gamePlayer = gamePlayer;
        this.moveSound = BattleSound.MYSTERY_BOX_MOVE;
        this.rollEndSound = BattleSound.MYSTERY_BOX_ROLL_END;
        this.rollSound = BattleSound.MYSTERY_BOX_ROLL;
        this.inUse = true;
        this.rotation = 0;
    }

    public boolean isInUse() {
        return inUse;
    }

    public boolean handleInteraction(GamePlayer gamePlayer) {
        return true;
    }

    public boolean handleLookInteraction(GamePlayer gamePlayer) {
        return false;
    }

    public void initState() {
        int pickupDelay = 1000;

        item = mysteryBox.getLeftSide().getWorld().dropItem(mysteryBox.getItemDropLocation(), mysteryBox.getWeapons()[0].getItemStack());
        item.setPickupDelay(pickupDelay);
        item.setVelocity(item.getVelocity().zero());

        mysteryBox.playChestAnimation(true);

        new BattleRunnable() {
            public void run() {
                if (!game.getState().isInProgress()) {
                    cancel();
                    return;
                }

                if (++rotation >= BOX_MAX_ROTATIONS) {
                    rotateChosenWeapon();
                    cancel();
                } else {
                    rotateRandomWeapon();
                }
            }
        }.runTaskTimer(BOX_ROTATION_DELAY, BOX_ROTATION_DURATION);
    }

    public void remove() {
        item.remove();
        mysteryBox.playChestAnimation(false);
    }

    private void rotateChosenWeapon() {
        ItemStack itemStack;
        MysteryBoxState state;
        Sound sound;

        if (!willMove()) {
            Hologram hologram = new Hologram(item.getLocation().add(0, -1, 0));

            itemStack = weapon.getItemStack();
            sound = rollEndSound;
            state = new DisplayState(game, mysteryBox, gamePlayer, hologram, item, weapon);
        } else {
            itemStack = new ItemStackBuilder(new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (byte) 1)).build();
            sound = moveSound;
            state = new MovingState(game, mysteryBox);
        }

        item.setItemStack(itemStack);

        mysteryBox.setCurrentWeapon(weapon);
        mysteryBox.setState(state);

        sound.play(game, item.getLocation());
    }

    private void rotateRandomWeapon() {
        Random random = new Random();
        Weapon[] weapons = mysteryBox.getWeapons();

        do {
            weapon = weapons[random.nextInt(weapons.length)];
        } while (mysteryBox.getCurrentWeapon() != null && mysteryBox.getCurrentWeapon().equals(weapon) || gamePlayer.getLoadout().getWeapon(weapon.getMetadata().getName()) != null);

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

package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.item.ItemMetadata;
import com.matsg.battlegrounds.api.item.WeaponContext;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public abstract class BattleWeapon extends BattleItem implements Weapon {

    protected GamePlayer gamePlayer;
    protected InternalsProvider internals;
    protected TaskRunner taskRunner;
    protected WeaponContext context;

    public BattleWeapon(ItemMetadata metadata, ItemStack itemStack, InternalsProvider internals, TaskRunner taskRunner) {
        super(metadata, itemStack);
        this.internals = internals;
        this.taskRunner = taskRunner;
    }

    public WeaponContext getContext() {
        return context;
    }

    public void setContext(WeaponContext context) {
        this.context = context;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public Weapon clone() {
        return (Weapon) super.clone();
    }

    protected void displayCircleEffect(Location location, int size, String effect, final int amount, int random) {
        final Location center = location.clone();

        for (double i = 0; i <= Math.PI; i += Math.PI / size) {
            double radius = Math.sin(i);
            double y = Math.cos(i) * 2;

            for (double a = 0; a < Math.PI * 2; a += Math.PI / size) {
                double x = Math.cos(a) * radius * 2;
                double z = Math.sin(a) * radius * 2;

                center.add(x, y, z);

                int delay = new Random().nextInt(random);
                int offset = 1;
                int speed = 0;

                taskRunner.runTaskLater(() -> internals.spawnParticle(location, effect, amount, offset, offset, offset, speed), delay);

                center.subtract(x, y, z);
            }
        }
    }

    public void onLeftClick(GamePlayer gamePlayer, PlayerInteractEvent event) {
        // The left click should not be executed if the player who clicked is not assigned to the weapon
        if (this.gamePlayer != gamePlayer) {
            return;
        }
        // Execute logic from subclasses
        onLeftClick(event);
    }

    public abstract void onLeftClick(PlayerInteractEvent event);

    public void onRightClick(GamePlayer gamePlayer, PlayerInteractEvent event) {
        // The right click should not be executed if the player who clicked is not assigned to the weapon
        if (this.gamePlayer != gamePlayer) {
            return;
        }
        // Execute logic from subclasses
        onRightClick(event);
    }

    public abstract void onRightClick(PlayerInteractEvent event);

    public void onSwap(GamePlayer gamePlayer, PlayerSwapHandItemsEvent event) {
        // The swap should not be executed if the player who clicked is not assigned to the weapon
        if (this.gamePlayer != gamePlayer) {
            return;
        }
        onSwap(event);
    }

    public abstract void onSwap(PlayerSwapHandItemsEvent event);

    public void onSwitch(GamePlayer gamePlayer, PlayerItemHeldEvent event) {
        // The switch should not be executed if the player who clicked is not assigned to the weapon
        if (this.gamePlayer != gamePlayer) {
            return;
        }
        onSwitch(event);
    }

    public abstract void onSwitch(PlayerItemHeldEvent event);

    public void remove() {
        if (game != null) {
            game.getItemRegistry().removeItem(this);
        }
        if (gamePlayer != null) {
            gamePlayer.getPlayer().getInventory().remove(itemStack);
        }
    }
}

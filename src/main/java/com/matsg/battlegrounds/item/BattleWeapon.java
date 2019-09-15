package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.ItemMetadata;
import com.matsg.battlegrounds.api.item.WeaponContext;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.util.BattleRunnable;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public abstract class BattleWeapon extends BattleItem implements Weapon {

    protected GamePlayer gamePlayer;
    protected WeaponContext context;

    public BattleWeapon(ItemMetadata metadata, ItemStack itemStack) {
        super(metadata, itemStack);
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

                new BattleRunnable() {
                    public void run() {
                        plugin.getVersion().spawnParticle(location, effect, amount, 1, 1, 1, 0);
                    }
                }.runTaskLater(new Random().nextInt(random));

                center.subtract(x, y, z);
            }
        }
    }

    public abstract void onLeftClick();

    public void onLeftClick(GamePlayer gamePlayer) {
        if (gamePlayer == null || gamePlayer != this.gamePlayer) {
            return;
        }
        onLeftClick();
    }

    public abstract void onRightClick();

    public void onRightClick(GamePlayer gamePlayer) {
        if (gamePlayer == null || gamePlayer != this.gamePlayer) {
            return;
        }
        onRightClick();
    }

    public abstract void onSwap();

    public void onSwap(GamePlayer gamePlayer) {
        if (gamePlayer == null || gamePlayer != this.gamePlayer) {
            return;
        }
        onSwap();
    }

    public abstract void onSwitch();

    public void onSwitch(GamePlayer gamePlayer) {
        if (gamePlayer == null || gamePlayer != this.gamePlayer) {
            return;
        }
        onSwitch();
    }

    public void remove() {
        if (game != null) {
            game.getItemRegistry().removeItem(this);
        }
        if (gamePlayer != null) {
            gamePlayer.getPlayer().getInventory().remove(itemStack);
        }
    }
}

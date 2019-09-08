package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.item.PowerUp;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpEffect;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.EnumTitle;
import org.bukkit.Location;
import org.bukkit.entity.Item;

import java.util.HashSet;
import java.util.Set;

public class ZombiesPowerUpManager implements PowerUpManager {

    private Game game;
    private Set<PowerUp> powerUps;

    public ZombiesPowerUpManager(Game game) {
        this.game = game;
        this.powerUps = new HashSet<>();
    }

    public void activatePowerUp(PowerUp powerUp) {
        powerUp.setActive(true);
        powerUp.getEffect().activate(() -> removePowerUp(powerUp));

        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            EnumTitle.POWERUP_ACTIVATE.send(gamePlayer.getPlayer(), new Placeholder("bg_powerup", powerUp.getName()));
        }

        BattleSound.POWERUP_ACTIVATE.play(game);
    }

    public void clear() {
        this.powerUps.clear();
    }

    public boolean contains(PowerUpEffect powerUpEffect) {
        return getPowerUp(powerUpEffect) != null;
    }

    public void dropPowerUp(PowerUp powerUp, Location location) {
        game.getItemRegistry().addItem(powerUp);
        powerUps.add(powerUp);

        Item item = location.getWorld().dropItem(location, powerUp.getItemStack());

        powerUp.getDroppedItems().add(item);

        new BattleRunnable() {
            int r = 0;
            public void run() {
                if (item.isDead()) {
                    cancel();
                    return;
                }
                if (++r >= 20) {
                    item.remove();
                    removePowerUp(powerUp);
                    cancel();
                }
            }
        }.runTaskTimer(400, 10);
    }

    private PowerUp getPowerUp(PowerUpEffect powerUpEffect) {
        for (PowerUp powerUp : powerUps) {
            if (powerUp.getName().equals(powerUpEffect.getName())) { // Compare just the names for now
                return powerUp;
            }
        }
        return null;
    }

    public int getPowerUpCount() {
        return powerUps.size();
    }

    public double getPowerUpDamage(double damage) {
        for (PowerUp powerUp : powerUps) {
            damage = powerUp.getEffect().modifyDamage(damage);
        }
        return damage;
    }

    public int getPowerUpPoints(int points) {
        for (PowerUp powerUp : powerUps) {
            points = powerUp.getEffect().modifyPoints(points);
        }
        return points;
    }

    public boolean isActive(PowerUpEffect powerUpEffect) {
        return (contains(powerUpEffect)) && (getPowerUp(powerUpEffect).isActive());
    }

    public void removePowerUp(PowerUp powerUp) {
        powerUp.remove();
        game.getItemRegistry().removeItem(powerUp);
        powerUps.remove(powerUp);
    }
}

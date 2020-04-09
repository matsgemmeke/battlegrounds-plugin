package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.item.PowerUp;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpEffect;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.EnumTitle;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class ZombiesPowerUpManager implements PowerUpManager {

    private Game game;
    private Set<PowerUp> powerUps;
    private TaskRunner taskRunner;

    public ZombiesPowerUpManager(Game game, TaskRunner taskRunner) {
        this.game = game;
        this.taskRunner = taskRunner;
        this.powerUps = new HashSet<>();
    }

    public void activatePowerUp(PowerUp powerUp) {
        powerUp.setActive(true);
        powerUp.getEffect().activate(powerUpEffect -> {
            removePowerUp(powerUp);
            if (powerUp.getEffect().getDuration() > 0) {
                displayPowerUpTitle(powerUp, EnumTitle.POWERUP_DEACTIVATE);
            }
        });

        displayPowerUpTitle(powerUp, EnumTitle.POWERUP_ACTIVATE);

        BattleSound.POWERUP_ACTIVATE.play(game);
    }

    public void clear() {
        powerUps.clear();
    }

    public void dropPowerUp(PowerUp powerUp, Location location) {
        game.getItemRegistry().addItem(powerUp);
        powerUps.add(powerUp);

        Item item = location.getWorld().dropItem(location, powerUp.getItemStack());

        powerUp.getDroppedItems().add(item);

        taskRunner.runTaskTimer(new BukkitRunnable() {
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
        }, 400, 10);
    }

    public boolean exists(PowerUpEffect powerUpEffect) {
        return getPowerUp(powerUpEffect) != null;
    }

    public int getPowerUpCount() {
        return powerUps.size();
    }

    public double getPowerUpDamage(double damage) {
        for (PowerUp powerUp : powerUps) {
            if (powerUp.isActive()) {
                damage = powerUp.getEffect().modifyDamage(damage);
            }
        }
        return damage;
    }

    public int getPowerUpPoints(int points) {
        for (PowerUp powerUp : powerUps) {
            if (powerUp.isActive()) {
                points = powerUp.getEffect().modifyPoints(points);
            }
        }
        return points;
    }

    public boolean isActive(PowerUpEffect powerUpEffect) {
        return (exists(powerUpEffect)) && (getPowerUp(powerUpEffect).isActive());
    }

    public void removePowerUp(PowerUp powerUp) {
        powerUp.remove();
        game.getItemRegistry().removeItem(powerUp);
        powerUps.remove(powerUp);
    }

    private void displayPowerUpTitle(PowerUp powerUp, EnumTitle title) {
        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            title.send(gamePlayer.getPlayer(), new Placeholder("bg_powerup", powerUp.getMetadata().getName()));
        }
    }

    private PowerUp getPowerUp(PowerUpEffect powerUpEffect) {
        for (PowerUp powerUp : powerUps) {
            if (powerUp.getMetadata().getName().equals(powerUpEffect.getName())) { // Compare just the names for now
                return powerUp;
            }
        }
        return null;
    }
}

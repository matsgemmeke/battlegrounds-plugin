package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.mode.zombies.item.PowerUp;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpEffect;
import org.bukkit.Location;

public interface PowerUpManager {

    void activatePowerUp(PowerUp powerUp);

    void clear();

    void dropPowerUp(PowerUp powerUp, Location location);

    boolean exists(PowerUpEffect powerUpEffect);

    int getPowerUpCount();

    double getPowerUpDamage(double damage);

    int getPowerUpPoints(int points);

    boolean isActive(PowerUpEffect powerUpEffect);

    void removePowerUp(PowerUp powerUp);
}

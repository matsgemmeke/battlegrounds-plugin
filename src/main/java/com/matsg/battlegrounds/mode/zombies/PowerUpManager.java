package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.mode.zombies.item.PowerUp;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpEffect;
import org.bukkit.Location;

public interface PowerUpManager {

    void activatePowerUp(PowerUp powerUp);

    void clear();

    boolean contains(PowerUpEffect powerUpEffect);

    void dropPowerUp(PowerUp powerUp, Location location);

    int getPowerUpCount();

    double getPowerUpDamage(double damage);

    int getPowerUpPoints(int points);

    boolean isActive(PowerUpEffect powerUpEffect);

    void removePowerUp(PowerUpEffect powerUpEffect);
}
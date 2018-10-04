package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.util.Sound;
import org.bukkit.Location;

public interface FireArm extends Weapon {

    FireArm clone();

    void cooldown(int time);

    int getAmmo();

    int getCooldown();

    int getMagazine();

    int getMagazineSize();

    int getMaxAmmo();

    DamageSource getProjectile();

    int getReloadDuration();

    boolean isReloading();

    boolean isShooting();

    void playReloadSound();

    void playReloadSound(Location location);

    void playShotSound();

    void playShotSound(Location location);

    void reload();

    void setAmmo(int ammo);

    void setMagazine(int magazine);

    void setReloadCancelled(boolean reloadCancelled);

    void setReloadDuration(int reloadDuration);

    void setReloading(boolean reloading);

    void setShooting(boolean shooting);

    void shoot();

    void shootProjectile();
}
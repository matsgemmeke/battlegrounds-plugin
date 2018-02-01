package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.util.Sound;

public interface FireArm extends Weapon {

    FireArm clone();

    void cooldown(int time);

    int getAmmo();

    int getCooldown();

    int getMagazine();

    int getMagazineSize();

    int getMaxAmmo();

    Projectile getProjectile();

    Sound[] getReloadSound();

    int getReloadTime();

    Sound[] getShootSound();

    boolean isReloading();

    boolean isShooting();

    void reload();

    void setAmmo(int ammo);

    void setMagazine(int magazine);

    void setReloadCancelled(boolean reloadCancelled);

    void setReloading(boolean reloading);

    void setReloadTime(int reloadTime);

    void setShooting(boolean shooting);

    void shoot();

    void shootProjectile();
}
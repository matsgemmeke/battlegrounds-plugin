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

    int getReloadDuration();

    Sound[] getReloadSound();

    Sound[] getShootSound();

    int getStartAmmo();

    boolean isReloading();

    boolean isShooting();

    void playReloadSound();

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
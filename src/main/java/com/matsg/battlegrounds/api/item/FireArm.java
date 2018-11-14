package com.matsg.battlegrounds.api.item;

import org.bukkit.entity.Entity;

public interface FireArm extends Weapon, Droppable {

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

    void playReloadSound(Entity entity);

    void playShotSound();

    void playShotSound(Entity entity);

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
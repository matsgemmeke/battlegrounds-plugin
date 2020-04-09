package com.matsg.battlegrounds.api.item;

import org.bukkit.entity.Entity;

public interface Firearm extends Weapon {

    int getAmmo();

    void setAmmo(int ammo);

    int getCooldown();

    int getMagazine();

    void setMagazine(int magazine);

    int getMagazineSize();

    int getMaxAmmo();

    DamageSource getProjectile();

    void setReloadCancelled(boolean reloadCancelled);

    int getReloadDuration();

    void setReloadDuration(int reloadDuration);

    boolean isReloading();

    void setReloading(boolean reloading);

    boolean isShooting();

    void setShooting(boolean shooting);

    Firearm clone();

    void cooldown(int time);

    void playReloadSound();

    void playReloadSound(Entity entity);

    void playShotSound();

    void playShotSound(Entity entity);

    void reload();

    void shoot();

    void shootProjectile();
}

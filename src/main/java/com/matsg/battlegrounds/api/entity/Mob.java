package com.matsg.battlegrounds.api.entity;

import com.matsg.battlegrounds.api.game.MobSpawn;
import org.bukkit.Location;

public interface Mob extends BattleEntity {

    void clearPathfinderGoals();

    double getAttackDamage();

    void setAttackDamage(double attackDamage);

    double getFollowRange();

    void setFollowRange(double followRange);

    double getMovementSpeed();

    void setMovementSpeed(double walkSpeed);

    boolean hasKnockback();

    void setKnockback(boolean knockback);

    boolean hasLoot();

    void setHostile(boolean hostile);

    boolean isSpawned();

    void setSpawned(boolean spawned);

    void resetDefaultPathfinderGoals();

    void setTarget(Location location);

    void spawn(MobSpawn mobSpawn);

    void updatePath();
}

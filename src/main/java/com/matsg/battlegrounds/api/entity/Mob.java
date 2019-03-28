package com.matsg.battlegrounds.api.entity;

import org.bukkit.Location;

public interface Mob extends BaseEntity {

    void clearPathfinderGoals();

    double getAttackDamage();

    double getFollowRange();

    double getMovementSpeed();

    boolean hasKnockback();

    boolean isHostile();

    void resetDefaultPathfinderGoals();

    void setAttackDamage(double attackDamage);

    void setFollowRange(double followRange);

    void setHostile(boolean hostile);

    void setKnockback(boolean knockback);

    void setMovementSpeed(double walkSpeed);

    void setTarget(Location location);

    // void spawn(MonsterSpawn monsterSpawn) throws MonsterSpawnException;

    void updatePath();
}

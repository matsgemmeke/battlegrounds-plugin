package com.matsg.battlegrounds.api.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface BattlegroundsEntity {

    double damage(double damage);

    Entity getBukkitEntity();

    float getHealth();

    void setHealth(float health);

    Location getLocation();

    float getMaxHealth();

    void setMaxHealth(float maxHealth);

    void remove();
}

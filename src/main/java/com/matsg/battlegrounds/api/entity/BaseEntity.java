package com.matsg.battlegrounds.api.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface BaseEntity {

    Entity getBukkitEntity();

    float getHealth();

    float getMaxHealth();

    Location getLocation();

    void remove();

    void setHealth(float health);

    void setMaxHealth(float maxHealth);
}

package com.matsg.battlegrounds.api.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface BaseEntity {

    Entity getBukkitEntity();

    double getHealth();

    double getMaxHealth();

    Location getLocation();

    void remove();

    void setHealth(double health);

    void setMaxHealth(double maxHealth);
}

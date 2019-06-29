package com.matsg.battlegrounds.api.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface BattleEntity {

    double damage(double damage);

    Entity getBukkitEntity();

    BattleEntityType getEntityType();

    float getHealth();

    void setHealth(float health);

    Location getLocation();

    float getMaxHealth();

    void setMaxHealth(float maxHealth);

    boolean isHostileTowards(GamePlayer gamePlayer);

    void remove();
}

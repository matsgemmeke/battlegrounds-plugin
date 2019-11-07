package com.matsg.battlegrounds.api.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Represents an entity that appears in a game of the Battlegrounds plugin.
 */
public interface BattleEntity {

    /**
     * Applies damage to the entity.
     *
     * @param damage the amount of damage to inflict
     * @return the amount of health the entity has after the damage was inflicted
     */
    double damage(double damage);

    /**
     * Gets the bukkit entity instance of the entity
     *
     * @return the bukkit entity
     */
    Entity getBukkitEntity();

    /**
     * Gets the entity type of the entity.
     *
     * @return the entity type
     */
    BattleEntityType getEntityType();

    /**
     * Gets the amount of health the entity has.
     *
     * @return the amount of health
     */
    float getHealth();

    /**
     * Sets the amount of health the entity has.
     *
     * @param health the amount of health
     */
    void setHealth(float health);

    /**
     * Gets the location of the entity.
     *
     * @return the entity's location
     */
    Location getLocation();

    /**
     * Gets the maximum amount of health the entity can receive
     *
     * @return the entity's maximum amount of health
     */
    float getMaxHealth();

    /**
     * Sets the maximum amount of health the entity can receive
     *
     * @param maxHealth the entity's maximum amount of health
     */
    void setMaxHealth(float maxHealth);

    /**
     * Gets whether this entity is hostile towards a certain player and can attack them.
     *
     * @param gamePlayer the player
     * @return whether the entity is hostile towards the player
     */
    boolean isHostileTowards(GamePlayer gamePlayer);

    /**
     * Removes the entity from the world.
     */
    void remove();
}

package com.matsg.battlegrounds.api.entity;

import com.matsg.battlegrounds.api.game.Obstacle;
import org.bukkit.Location;

/**
 * Represents a hostile entity that appears in a game of the Battlegrounds plugin.
 */
public interface Mob extends BattleEntity {

    /**
     * Clears the current pathfinder goals of the mob.
     */
    void clearPathfinderGoals();

    /**
     * Gets the attack damage of the mob.
     *
     * @return the mob's attack damage
     */
    double getAttackDamage();

    /**
     * Sets the attack damage of the mob.
     *
     * @param attackDamage the mob's attack damage
     */
    void setAttackDamage(double attackDamage);

    /**
     * Gets the follow range of the mob.
     *
     * @return the mob's follow range
     */
    double getFollowRange();

    /**
     * Sets the follow range of the mob.
     *
     * @param followRange the mob's follow range
     */
    void setFollowRange(double followRange);

    /**
     * Gets the movement speed of the mob.
     *
     * @return the mob's movement speed
     */
    double getMovementSpeed();

    /**
     * Sets the movement speed of the mob.
     *
     * @param movementSpeed the mob's movement speed
     */
    void setMovementSpeed(double movementSpeed);

    /**
     * Gets whether the mob receives knockback upon attacks.
     *
     * @return whether the mob receives knockback physics
     */
    boolean hasKnockback();

    /**
     * Sets whether the mob receives knockback upon attacks.
     *
     * @param knockback whether the mob receives knockback physics
     */
    void setKnockback(boolean knockback);

    /**
     * Gets whether the mob has loot.
     *
     * @return whether the mob has loot
     */
    boolean hasLoot();

    /**
     * Sets whether the mob is hostile and will attack players.
     *
     * @param hostile whether the mob is hostile
     */
    void setHostile(boolean hostile);

    /**
     * Gets whether the mob has spawned into the game.
     *
     * @return whether the mob has spawned
     */
    boolean isSpawned();

    /**
     * Sets whether the mob has spawned into the game.
     *
     * @param spawned whether the mob has spawned
     */
    void setSpawned(boolean spawned);

    /**
     * Resets the mob's current pathfinder goals
     */
    void resetDefaultPathfinderGoals();

    /**
     * Sets the target of the mob.
     *
     * @param location the target location
     */
    void setTarget(Location location);

    /**
     * Spawns the mob at the given location.
     *
     * @param location the spawn location
     * @param obstacle an optionable obstacle
     */
    void spawn(Location location, Obstacle obstacle);

    /**
     * Updates the mob's path to their target.
     */
    void updatePath();
}

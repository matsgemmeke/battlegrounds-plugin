package com.matsg.battlegrounds.mode.zombies.item;

import org.bukkit.Material;

import java.util.function.Consumer;

public interface PowerUpEffect {

    /**
     * Gets the duration of the power up effect.
     *
     * @return the duration
     */
    int getDuration();

    /**
     * Sets the duration of the power up effect.
     *
     * @param duration the duration
     */
    void setDuration(int duration);

    /**
     * Gets the material icon of the power up effect.
     *
     * @return the material
     */
    Material getMaterial();

    /**
     * Gets the name indication of the power up effect.
     *
     * @return the name
     */
    String getName();

    /**
     * Activates the power up effect in a game.
     *
     * @param callback the callback method
     */
    void activate(Consumer<PowerUpEffect> callback);

    /**
     * Gets whether the power up effect applicable for activation with the game's current state.
     *
     * @return whether the effect is applicable for activation
     */
    boolean isApplicableForActivation();

    /**
     * Modifies the amount of damage applied by a player.
     *
     * @param damage the damage
     * @return either the same or a modified value
     */
    double modifyDamage(double damage);

    /**
     * Modifies the amount of points gained by a player.
     *
     * @param points the amount of points
     * @return either the same or a modified value
     */
    int modifyPoints(int points);
}

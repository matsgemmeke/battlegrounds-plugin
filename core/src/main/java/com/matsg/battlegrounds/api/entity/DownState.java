package com.matsg.battlegrounds.api.entity;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Describes the state of a player when they are downed.
 */
public interface DownState {

    /**
     * Gets the player who is downed.
     *
     * @return the downed player
     */
    @NotNull
    GamePlayer getGamePlayer();

    /**
     * Gets the location where the downed player is downed.
     *
     * @return the location of the downed player.
     */
    @NotNull
    Location getLocation();

    /**
     * Gets the maximum distance a reviving player can be from the downed player to revive them.
     *
     * @return the maximum reviving distance
     */
    double getMaxRevivingDistance();

    /**
     * Sets the maximum distance a reviving player can be from the downed player to revive them.
     *
     * @param maxRevivingDistance the maximum reviving distance
     */
    void setMaxRevivingDistance(double maxRevivingDistance);

    /**
     * Gets the player who is reviving the downed player. Returns null if the downed player is not being revived.
     *
     * @return the player reviving the downed player
     */
    @Nullable
    GamePlayer getReviver();

    /**
     * Sets the player who is reviving the downed player.
     *
     * @param reviver the player reviving the downed player
     */
    void setReviver(@Nullable GamePlayer reviver);

    /**
     * Disposes of the down state's props.
     */
    void dispose();

    /**
     * Starts the down state.
     */
    void run();
}

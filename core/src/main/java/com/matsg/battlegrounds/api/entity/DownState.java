package com.matsg.battlegrounds.api.entity;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DownState {

    /**
     * Gets the player who is downed.
     *
     * @return the downed player
     */
    @NotNull
    GamePlayer getGamePlayer();

    /**
     * Gets the location where the downed player was downed.
     *
     * @return the location of the downed player.
     */
    @NotNull
    Location getLocation();

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

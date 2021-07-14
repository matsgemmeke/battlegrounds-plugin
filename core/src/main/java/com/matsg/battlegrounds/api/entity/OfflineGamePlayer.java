package com.matsg.battlegrounds.api.entity;

import java.util.UUID;

/**
 * Represents a player that may or may not be online.
 */
public interface OfflineGamePlayer {

    /**
     * Gets the amount of times the player has died.
     *
     * @return the amount of deaths
     */
    int getDeaths();

    /**
     * Sets the amount of times the player has died.
     *
     * @param deaths the amount of deaths
     */
    void setDeaths(int deaths);

    /**
     * Gets the amount of exp the player has.
     *
     * @return the amount of exp
     */
    int getExp();

    /**
     * Sets the amount of exp the player has.
     *
     * @param exp the amount of exp
     */
    void setExp(int exp);

    /**
     * Gets the amount of headshots the player has.
     *
     * @return the amount of headshots
     */
    int getHeadshots();

    /**
     * Sets the amount of headshots the player has.
     *
     * @param headshots the amount of headshots
     */
    void setHeadshots(int headshots);

    /**
     * Gets the amount of kills the player has.
     *
     * @return the amount of kills
     */
    int getKills();

    /**
     * Sets the amount of kills that player has.
     *
     * @param kills the amount of kills
     */
    void setKills(int kills);

    /**
     * Gets the name of the player
     *
     * @return the player's name
     */
    String getName();

    /**
     * Gets the UUID of the player
     *
     * @return the player's UUID
     */
    UUID getUUID();

    /**
     * Gets whether the player is currently online
     *
     * @return whether the player is online
     */
    boolean isOnline();
}

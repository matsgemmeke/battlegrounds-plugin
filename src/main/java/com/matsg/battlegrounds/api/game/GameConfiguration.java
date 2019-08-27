package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.storage.Yaml;

public interface GameConfiguration {

    /**
     * Gets the different gamemodes the game runs.
     *
     * @return the game's gamemodes
     */
    GameMode[] getGameModes();

    /**
     * Gets the length of the lobby countdown.
     *
     * @return the lobby countdown length
     */
    int getLobbyCountdown();

    /**
     * Gets the maximum amount of players allowed in the game.
     *
     * @return the maximum allowed amount of players
     */
    int getMaxPlayers();

    /**
     * Gets the minimum amount of players allowed in the game.
     *
     * @return the minimum allowed amount of players
     */
    int getMinPlayers();

    /**
     * Saves the configuration to a yaml file.
     *
     * @param yaml the yaml file
     */
    void saveConfiguration(Yaml yaml);
}

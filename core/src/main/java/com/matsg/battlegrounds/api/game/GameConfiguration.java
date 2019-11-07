package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.storage.Yaml;

import java.util.List;

public interface GameConfiguration {

    /**
     * Gets the different gamemode types the game is configured to run.
     *
     * @return the game's gamemode types
     */
    List<String> getGameModeTypes();

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

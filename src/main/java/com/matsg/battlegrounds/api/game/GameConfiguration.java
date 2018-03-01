package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.config.Yaml;

public interface GameConfiguration {

    int getCountdownLength();

    GameMode[] getGameModes();

    int getMaxPlayers();

    int getMinPlayers();

    void saveConfiguration(Yaml yaml);
}
package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.storage.Yaml;

public interface GameConfiguration {

    GameMode[] getGameModes();

    int getLobbyCountdown();

    int getMaxPlayers();

    int getMinPlayers();

    void saveConfiguration(Yaml yaml);
}

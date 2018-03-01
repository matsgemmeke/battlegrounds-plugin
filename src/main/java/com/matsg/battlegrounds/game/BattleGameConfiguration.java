package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.game.GameConfiguration;
import com.matsg.battlegrounds.api.game.GameMode;

public class BattleGameConfiguration implements GameConfiguration {

    public static final BattleGameConfiguration DEFAULT = getDefaultConfiguration();

    private GameMode[] gameModes;
    private int countdownLength, maxPlayers, minPlayers;

    public BattleGameConfiguration(GameMode[] gameModes, int maxPlayers, int minPlayers, int countdownLength) {
        this.countdownLength = countdownLength;
        this.gameModes = gameModes;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
    }

    private static BattleGameConfiguration getDefaultConfiguration() {
        return new BattleGameConfiguration(
                new GameMode[0],
                4,
                1,
                30
        );
    }

    public int getCountdownLength() {
        return countdownLength;
    }

    public GameMode[] getGameModes() {
        return gameModes;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }
    
    public void saveConfiguration(Yaml yaml) {
        yaml.set("_config.countdown", countdownLength);
        yaml.set("_config.maxplayers", maxPlayers);
        yaml.set("_config.minplayers", minPlayers);
    }
}
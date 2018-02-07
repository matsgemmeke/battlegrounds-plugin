package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.game.GameConfiguration;

public class BattleGameConfiguration implements GameConfiguration {

    public static final BattleGameConfiguration DEFAULT = new BattleGameConfiguration(4, 1, 30);

    private int countdownLength, maxPlayers, minPlayers;

    public BattleGameConfiguration(int maxPlayers, int minPlayers, int countdownLength) {
        this.countdownLength = countdownLength;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
    }

    public int getCountdownLength() {
        return countdownLength;
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
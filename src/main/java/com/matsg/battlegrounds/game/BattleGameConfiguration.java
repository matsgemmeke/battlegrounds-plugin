package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.game.GameConfiguration;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.game.gamemode.FreeForAll;
import com.matsg.battlegrounds.game.gamemode.TeamDeathmatch;

import java.util.ArrayList;
import java.util.List;

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
                new GameMode[] { new FreeForAll(null, null), new TeamDeathmatch(null, null) },
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

    private List<String> getGameModeNames() {
        List<String> list = new ArrayList<>();
        for (GameMode gameMode : gameModes) {
            list.add(gameMode.getName().replaceAll(" ", "_").toUpperCase());
        }
        return list;
    }
    
    public void saveConfiguration(Yaml yaml) {
        yaml.set("_config.countdown", countdownLength);
        yaml.set("_config.gamemodes", getGameModeNames());
        yaml.set("_config.maxplayers", maxPlayers);
        yaml.set("_config.minplayers", minPlayers);
        yaml.save();
    }
}
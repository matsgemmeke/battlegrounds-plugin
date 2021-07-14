package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.storage.Yaml;
import com.matsg.battlegrounds.api.game.GameConfiguration;

import java.util.*;

public class BattleGameConfiguration implements GameConfiguration {

    private int lobbyCountdown, maxPlayers, minPlayers;
    private List<String> gameModeTypes;

    public BattleGameConfiguration(List<String> gameModeTypes, int maxPlayers, int minPlayers, int lobbyCountdown) {
        this.gameModeTypes = gameModeTypes;
        this.lobbyCountdown = lobbyCountdown;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
    }

    public static BattleGameConfiguration getDefaultConfiguration() {
        return new BattleGameConfiguration(
                new ArrayList<>(Arrays.asList("FREE_FOR_ALL", "TEAM_DEATHMATCH")),
                12,
                2,
                60
        );
    }

    public List<String> getGameModeTypes() {
        return gameModeTypes;
    }

    public int getLobbyCountdown() {
        return lobbyCountdown;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }
    
    public void saveConfiguration(Yaml yaml) {
        yaml.set("config.gamemodes", gameModeTypes);
        yaml.set("config.lobbycountdown", lobbyCountdown);
        yaml.set("config.maxplayers", maxPlayers);
        yaml.set("config.minplayers", minPlayers);
        yaml.save();
    }
}

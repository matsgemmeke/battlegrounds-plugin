package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.storage.Yaml;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameConfiguration;
import com.matsg.battlegrounds.api.gamemode.GameMode;
import com.matsg.battlegrounds.gamemode.GameModeType;

import java.util.ArrayList;
import java.util.List;

public class BattleGameConfiguration implements GameConfiguration {

    private GameMode[] gameModes;
    private int gameCountdown, lobbyCountdown, maxPlayers, minPlayers;

    public BattleGameConfiguration(GameMode[] gameModes, int maxPlayers, int minPlayers, int gameCountdown, int lobbyCountdown) {
        this.gameCountdown = gameCountdown;
        this.gameModes = gameModes;
        this.lobbyCountdown = lobbyCountdown;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
    }

    public static BattleGameConfiguration getDefaultConfiguration(Game game) {
        return new BattleGameConfiguration(
                new GameMode[] { GameModeType.FREE_FOR_ALL.getInstance(game), GameModeType.TEAM_DEATHMATCH.getInstance(game) },
                12,
                2,
                15,
                60
        );
    }

    public int getGameCountdown() {
        return gameCountdown;
    }

    public int getLobbyCountdown() {
        return lobbyCountdown;
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
            list.add(GameModeType.getByGameMode(gameMode.getClass()).toString());
        }
        return list;
    }
    
    public void saveConfiguration(Yaml yaml) {
        yaml.set("_config.gamecountdown", gameCountdown);
        yaml.set("_config.gamemodes", getGameModeNames());
        yaml.set("_config.lobbycountdown", lobbyCountdown);
        yaml.set("_config.maxplayers", maxPlayers);
        yaml.set("_config.minplayers", minPlayers);
        yaml.save();
    }
}

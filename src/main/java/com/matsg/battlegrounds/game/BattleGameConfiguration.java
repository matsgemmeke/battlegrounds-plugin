package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.game.GameConfiguration;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.gamemode.ffa.FreeForAll;
import com.matsg.battlegrounds.gamemode.tdm.TeamDeathmatch;

import java.util.ArrayList;
import java.util.List;

public class BattleGameConfiguration implements GameConfiguration {

    public static final BattleGameConfiguration DEFAULT = getDefaultConfiguration();

    private GameMode[] gameModes;
    private int gameCountdown, lobbyCountdown, maxPlayers, minPlayers;

    public BattleGameConfiguration(GameMode[] gameModes, int maxPlayers, int minPlayers, int gameCountdown, int lobbyCountdown) {
        this.gameCountdown = gameCountdown;
        this.lobbyCountdown = lobbyCountdown;
        this.gameModes = gameModes;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
    }

    private static BattleGameConfiguration getDefaultConfiguration() {
        return new BattleGameConfiguration(
                new GameMode[] { new FreeForAll(null, null), new TeamDeathmatch(null, null) },
                4,
                1,
                60,
                15
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
            list.add(gameMode.getName().replaceAll(" ", "_").toUpperCase());
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
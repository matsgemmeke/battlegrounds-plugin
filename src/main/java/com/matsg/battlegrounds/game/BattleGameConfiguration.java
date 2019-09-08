package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.storage.Yaml;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameConfiguration;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.mode.GameModeFactory;
import com.matsg.battlegrounds.mode.GameModeType;

import java.util.ArrayList;
import java.util.List;

public class BattleGameConfiguration implements GameConfiguration {

    private GameMode[] gameModes;
    private int lobbyCountdown, maxPlayers, minPlayers;

    public BattleGameConfiguration(GameMode[] gameModes, int maxPlayers, int minPlayers, int lobbyCountdown) {
        this.gameModes = gameModes;
        this.lobbyCountdown = lobbyCountdown;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
    }

    public static BattleGameConfiguration getDefaultConfiguration(Game game) {
        Battlegrounds plugin = BattlegroundsPlugin.getPlugin();
        GameModeFactory gameModeFactory = new GameModeFactory(plugin, plugin.getTranslator(), plugin.getVersion());

        return new BattleGameConfiguration(
                new GameMode[] {
                        gameModeFactory.make(game, GameModeType.FREE_FOR_ALL),
                        gameModeFactory.make(game, GameModeType.TEAM_DEATHMATCH)
                },
                12,
                2,
                60
        );
    }

    public GameMode[] getGameModes() {
        return gameModes;
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

    private List<String> getGameModeNames() {
        List<String> list = new ArrayList<>();
        for (GameMode gameMode : gameModes) {
            list.add(gameMode.getId());
        }
        return list;
    }
    
    public void saveConfiguration(Yaml yaml) {
        yaml.set("config.gamemodes", getGameModeNames());
        yaml.set("config.lobbycountdown", lobbyCountdown);
        yaml.set("config.maxplayers", maxPlayers);
        yaml.set("config.minplayers", minPlayers);
        yaml.save();
    }
}

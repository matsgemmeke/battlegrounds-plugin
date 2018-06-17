package com.matsg.battlegrounds.gamemode;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.AbstractYaml;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.gamemode.ffa.FreeForAll;
import com.matsg.battlegrounds.gamemode.tdm.TeamDeathmatch;

public enum GameModeType {

    FREE_FOR_ALL(1, FreeForAll.class) {
        public GameMode getInstance(Game game) {
            try {
                return new FreeForAll(game, new AbstractYaml(plugin, plugin.getDataFolder().getPath() + "/gamemodes", "ffa.yml", true) { } );
            } catch (Exception e) {
                return null;
            }
        }
    },
    TEAM_DEATHMATCH(2, TeamDeathmatch.class) {
        public GameMode getInstance(Game game) {
            try {
                return new TeamDeathmatch(game, new AbstractYaml(plugin, plugin.getDataFolder().getPath() + "/gamemodes", "tdm.yml", true) { } );
            } catch (Exception e) {
                return null;
            }
        }
    };

    private static Battlegrounds plugin = BattlegroundsPlugin.getPlugin();
    private Class gameModeClass;
    private int id;

    GameModeType(int id, Class gameModeClass) {
        this.id = id;
        this.gameModeClass = gameModeClass;
    }

    public static GameModeType getByGameMode(Class gameModeClass) {
        for (GameModeType gameModeType : values()) {
            if (gameModeType.gameModeClass == gameModeClass) {
                return gameModeType;
            }
        }
        return null;
    }

    public static GameModeType valueOf(int id) {
        for (GameModeType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }

    public abstract GameMode getInstance(Game game);
}
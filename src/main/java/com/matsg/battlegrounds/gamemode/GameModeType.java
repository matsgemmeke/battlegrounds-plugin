package com.matsg.battlegrounds.gamemode;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.AbstractYaml;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.gamemode.GameMode;
import com.matsg.battlegrounds.gamemode.cbt.CombatTraining;
import com.matsg.battlegrounds.gamemode.ffa.FreeForAll;
import com.matsg.battlegrounds.gamemode.tdm.TeamDeathmatch;

import java.io.File;
import java.io.IOException;

public enum GameModeType {

    FREE_FOR_ALL(1, FreeForAll.class) {
        public GameMode getInstance(Game game) {
            try {
                return new FreeForAll(plugin, game, new AbstractYaml(plugin, filePath(game).getPath(), "ffa.yml", true) { } );
            } catch (IOException e) {
                return null;
            }
        }
    },
    TEAM_DEATHMATCH(2, TeamDeathmatch.class) {
        public GameMode getInstance(Game game) {
            try {
                return new TeamDeathmatch(plugin, game, new AbstractYaml(plugin, filePath(game).getPath(), "tdm.yml", true) { } );
            } catch (IOException e) {
                return null;
            }
        }
    },
    COMBAT_TRAINING(3, CombatTraining.class) {
        public GameMode getInstance(Game game) {
            try {
                return new CombatTraining(plugin, game, new AbstractYaml(plugin, filePath(game).getPath(), "cbt.yml", true) { });
            } catch (IOException e) {
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

    private static File filePath(Game game) {
        return new File(plugin.getDataFolder().getPath() + "/data/game_" + game.getId() + "/gamemodes");
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
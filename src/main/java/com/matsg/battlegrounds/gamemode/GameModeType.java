package com.matsg.battlegrounds.gamemode;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.config.AbstractYaml;
import com.matsg.battlegrounds.gamemode.ffa.FreeForAll;
import com.matsg.battlegrounds.gamemode.tdm.TeamDeathmatch;

public enum GameModeType {

    FREE_FOR_ALL(1) {
        public GameMode getInstance(Game game) {
            try {
                return new FreeForAll(game, new AbstractYaml(plugin, plugin.getDataFolder().getPath() + "/gamemodes", "free_for_all.yml", true) { } );
            } catch (Exception e) {
                return null;
            }
        }
    },
    TEAM_DEATHMATCH(2) {
        public GameMode getInstance(Game game) {
            try {
                return new TeamDeathmatch(game, new AbstractYaml(plugin, plugin.getDataFolder().getPath() + "/gamemodes", "team_deathmatch.yml", true) { } );
            } catch (Exception e) {
                return null;
            }
        }
    };

    private static Battlegrounds plugin = BattlegroundsPlugin.getPlugin();
    private int id;

    GameModeType(int id) {
        this.id = id;
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
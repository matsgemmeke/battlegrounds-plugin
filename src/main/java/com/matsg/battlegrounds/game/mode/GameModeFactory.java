package com.matsg.battlegrounds.game.mode;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.game.mode.ffa.FreeForAll;
import com.matsg.battlegrounds.game.mode.tdm.TeamDeathmatch;
import com.matsg.battlegrounds.game.mode.zombies.Zombies;
import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.storage.BattleCacheYaml;

import java.io.IOException;

public class GameModeFactory {

    private static Battlegrounds plugin = BattlegroundsPlugin.getPlugin();

    public GameMode make(Game game, GameModeType gameModeType) {
        switch (gameModeType) {
            case FREE_FOR_ALL:
                try {
                    return new FreeForAll(plugin, game, new BattleCacheYaml(plugin, "data/game_" + game.getId() + "/gamemodes/ffa.yml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            case TEAM_DEATHMATCH:
                try {
                    return new TeamDeathmatch(plugin, game, new BattleCacheYaml(plugin, "data/game_" + game.getId() + "/gamemodes/tdm.yml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            case ZOMBIES:
                try {
                    return new Zombies(plugin, game, new BattleCacheYaml(plugin, "data/game_" + game.getId() + "/gamemodes/zombies.yml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            default:
                throw new FactoryCreationException("Invalid game mode type \"" + gameModeType + "\"");
        }
    }
}

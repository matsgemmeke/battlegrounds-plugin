package com.matsg.battlegrounds.game.mode;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.game.mode.ffa.FFAConfig;
import com.matsg.battlegrounds.game.mode.ffa.FreeForAll;
import com.matsg.battlegrounds.game.mode.tdm.TDMConfig;
import com.matsg.battlegrounds.game.mode.tdm.TeamDeathmatch;
import com.matsg.battlegrounds.game.mode.zombies.Zombies;
import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.game.mode.zombies.ZombiesConfig;

import java.io.IOException;

public class GameModeFactory {

    private Battlegrounds plugin;
    private Translator translator;

    public GameModeFactory(Battlegrounds plugin, Translator translator) {
        this.plugin = plugin;
        this.translator = translator;
    }

    public GameMode make(Game game, GameModeType gameModeType) {
        switch (gameModeType) {
            case FREE_FOR_ALL:
                try {
                    return new FreeForAll(plugin, game, translator, new FFAConfig(plugin, plugin.getDataFolder().getPath() + "/data/game_" + game.getId() + "/gamemodes/"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            case TEAM_DEATHMATCH:
                try {
                    return new TeamDeathmatch(plugin, game, translator, new TDMConfig(plugin, plugin.getDataFolder().getPath() + "/data/game_" + game.getId() + "/gamemodes/"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            case ZOMBIES:
                try {
                    return new Zombies(plugin, game, translator, new ZombiesConfig(plugin, plugin.getDataFolder().getPath() + "/data/game_" + game.getId() + "/gamemodes/"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            default:
                throw new FactoryCreationException("Invalid game mode type \"" + gameModeType + "\"");
        }
    }
}

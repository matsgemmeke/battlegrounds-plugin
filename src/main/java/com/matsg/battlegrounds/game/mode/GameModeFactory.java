package com.matsg.battlegrounds.game.mode;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.game.mode.ffa.FFAConfig;
import com.matsg.battlegrounds.game.mode.ffa.FreeForAll;
import com.matsg.battlegrounds.game.mode.shared.SpawnByTeamBehavior;
import com.matsg.battlegrounds.game.mode.shared.SpawnRandomlyBehavior;
import com.matsg.battlegrounds.game.mode.shared.SpawningBehavior;
import com.matsg.battlegrounds.game.mode.tdm.TDMConfig;
import com.matsg.battlegrounds.game.mode.tdm.TeamDeathmatch;
import com.matsg.battlegrounds.game.mode.zombies.Zombies;
import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.game.mode.zombies.ZombiesConfig;
import com.matsg.battlegrounds.game.objective.EliminationObjective;
import com.matsg.battlegrounds.game.objective.ScoreObjective;
import com.matsg.battlegrounds.game.objective.TimeObjective;

import java.io.IOException;

public class GameModeFactory {

    private Battlegrounds plugin;
    private Translator translator;

    public GameModeFactory(Battlegrounds plugin, Translator translator) {
        this.plugin = plugin;
        this.translator = translator;
    }

    public GameMode make(Game game, GameModeType gameModeType) {
        Arena arena = game.getArena();

        switch (gameModeType) {
            case FREE_FOR_ALL:
                try {
                    SpawningBehavior spawningBehavior = new SpawnRandomlyBehavior(arena);
                    FFAConfig config = new FFAConfig(plugin, plugin.getDataFolder().getPath() + "/data/game_" + game.getId() + "/gamemodes");

                    GameMode gameMode = new FreeForAll(plugin, game, translator, spawningBehavior, config);
                    gameMode.addObjective(new EliminationObjective(game, 2));
                    gameMode.addObjective(new ScoreObjective(game, config.getKillsToWin()));
                    gameMode.addObjective(new TimeObjective(game, config.getTimeLimit()));

                    return gameMode;
                } catch (IOException e) {
                    throw new FactoryCreationException("Could not create gamemode of type \"" + gameModeType + "\"", e);
                }
            case TEAM_DEATHMATCH:
                try {
                    SpawningBehavior spawningBehavior = new SpawnByTeamBehavior(arena);
                    TDMConfig config = new TDMConfig(plugin, plugin.getDataFolder().getPath() + "/data/game_" + game.getId() + "/gamemodes");

                    GameMode gameMode = new TeamDeathmatch(plugin, game, translator, spawningBehavior, config);
                    gameMode.addObjective(new EliminationObjective(game, 2));
                    gameMode.addObjective(new ScoreObjective(game, config.getKillsToWin()));
                    gameMode.addObjective(new TimeObjective(game, config.getTimeLimit()));

                    return gameMode;
                } catch (IOException e) {
                    throw new FactoryCreationException("Could not create gamemode of type \"" + gameModeType + "\"", e);
                }
            case ZOMBIES:
                try {
                    SpawningBehavior spawningBehavior = new SpawnRandomlyBehavior(arena);
                    ZombiesConfig config = new ZombiesConfig(plugin, plugin.getDataFolder().getPath() + "/data/game_" + game.getId() + "/gamemodes");

                    GameMode gameMode = new Zombies(plugin, game, translator, spawningBehavior, config);
                    gameMode.addObjective(new EliminationObjective(game, 1));

                    return gameMode;
                } catch (IOException e) {
                    throw new FactoryCreationException("Could not create gamemode of type \"" + gameModeType + "\"", e);
                }
            default:
                throw new FactoryCreationException("Invalid game mode type \"" + gameModeType + "\"");
        }
    }
}

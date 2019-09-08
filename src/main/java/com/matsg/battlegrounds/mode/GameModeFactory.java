package com.matsg.battlegrounds.mode;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.gui.View;
import com.matsg.battlegrounds.mode.ffa.FFAConfig;
import com.matsg.battlegrounds.mode.ffa.FreeForAll;
import com.matsg.battlegrounds.mode.shared.SpawnByTeamBehavior;
import com.matsg.battlegrounds.mode.shared.SpawnRandomlyBehavior;
import com.matsg.battlegrounds.mode.shared.SpawningBehavior;
import com.matsg.battlegrounds.mode.tdm.TDMConfig;
import com.matsg.battlegrounds.mode.tdm.TeamDeathmatch;
import com.matsg.battlegrounds.mode.zombies.*;
import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.game.objective.EliminationObjective;
import com.matsg.battlegrounds.game.objective.ScoreObjective;
import com.matsg.battlegrounds.game.objective.TimeObjective;
import com.matsg.battlegrounds.mode.zombies.gui.ZombiesOverviewView;

import java.io.IOException;

public class GameModeFactory {

    private Battlegrounds plugin;
    private Translator translator;
    private Version version;

    public GameModeFactory(Battlegrounds plugin, Translator translator, Version version) {
        this.plugin = plugin;
        this.translator = translator;
        this.version = version;
    }

    public GameMode make(Game game, GameModeType gameModeType) {
        Arena arena = game.getArena();
        GameMode gameMode;

        switch (gameModeType) {
            case FREE_FOR_ALL:
                try {
                    FFAConfig config = new FFAConfig(plugin, plugin.getDataFolder().getPath() + "/data/game_" + game.getId() + "/gamemodes");
                    SpawningBehavior spawningBehavior = new SpawnRandomlyBehavior(arena);

                    gameMode = new FreeForAll(plugin, game, translator, spawningBehavior, config);
                    gameMode.addObjective(new EliminationObjective(game, 2));
                    gameMode.addObjective(new ScoreObjective(game, config.getKillsToWin()));
                    gameMode.addObjective(new TimeObjective(game, config.getTimeLimit()));

                    break;
                } catch (IOException e) {
                    throw new FactoryCreationException("Could not create gamemode of type \"" + gameModeType + "\"", e);
                }
            case TEAM_DEATHMATCH:
                try {
                    TDMConfig config = new TDMConfig(plugin, plugin.getDataFolder().getPath() + "/data/game_" + game.getId() + "/gamemodes");
                    SpawningBehavior spawningBehavior = new SpawnByTeamBehavior(arena);

                    gameMode = new TeamDeathmatch(plugin, game, translator, spawningBehavior, config);
                    gameMode.addObjective(new EliminationObjective(game, 2));
                    gameMode.addObjective(new ScoreObjective(game, config.getKillsToWin()));
                    gameMode.addObjective(new TimeObjective(game, config.getTimeLimit()));

                    break;
                } catch (IOException e) {
                    throw new FactoryCreationException("Could not create gamemode of type \"" + gameModeType + "\"", e);
                }
            case ZOMBIES:
                try {
                    ZombiesConfig config = new ZombiesConfig(plugin, plugin.getDataFolder().getPath() + "/data/game_" + game.getId() + "/gamemodes");
                    PerkManager perkManager = new ZombiesPerkManager();
                    PowerUpManager powerUpManager = new ZombiesPowerUpManager(game);
                    SpawningBehavior spawningBehavior = new SpawnRandomlyBehavior(arena);

                    gameMode = new Zombies(plugin, game, translator, spawningBehavior, perkManager, powerUpManager, version, config);
                    gameMode.addObjective(new EliminationObjective(game, 1));

                    break;
                } catch (IOException e) {
                    throw new FactoryCreationException("Could not create gamemode of type \"" + gameModeType + "\"", e);
                }
            default:
                throw new FactoryCreationException("Invalid game mode type \"" + gameModeType + "\"");
        }

        return gameMode;
    }
}

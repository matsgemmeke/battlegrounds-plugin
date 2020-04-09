package com.matsg.battlegrounds.mode;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.gui.ViewFactory;
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
import org.bukkit.Server;

import java.io.IOException;

public class GameModeFactory {

    private Battlegrounds plugin;
    private InternalsProvider internals;
    private TaskRunner taskRunner;
    private Translator translator;
    private ViewFactory viewFactory;

    public GameModeFactory(Battlegrounds plugin, InternalsProvider internals, TaskRunner taskRunner, Translator translator, ViewFactory viewFactory) {
        this.plugin = plugin;
        this.internals = internals;
        this.taskRunner = taskRunner;
        this.translator = translator;
        this.viewFactory = viewFactory;
    }

    public GameMode make(Game game, GameModeType gameModeType) {
        Arena arena = game.getArena();
        Server server = plugin.getServer();

        GameMode gameMode;

        switch (gameModeType) {
            case FREE_FOR_ALL:
                try {
                    FFAConfig config = new FFAConfig(plugin.getDataFolder().getPath() + "/data/game_" + game.getId() + "/gamemodes", plugin.getResource("ffa.yml"), server);
                    SpawningBehavior spawningBehavior = new SpawnRandomlyBehavior(arena);

                    gameMode = new FreeForAll(plugin, game, spawningBehavior, taskRunner, translator, viewFactory, config);
                    gameMode.addObjective(new EliminationObjective(game, 2));
                    gameMode.addObjective(new ScoreObjective(game, config.getKillsToWin()));
                    gameMode.addObjective(new TimeObjective(game, config.getTimeLimit()));

                    break;
                } catch (IOException e) {
                    throw new FactoryCreationException("Could not create gamemode of type \"" + gameModeType + "\"", e);
                }
            case TEAM_DEATHMATCH:
                try {
                    TDMConfig config = new TDMConfig(plugin.getDataFolder().getPath() + "/data/game_" + game.getId() + "/gamemodes", plugin.getResource("tdm.yml"), server);
                    SpawningBehavior spawningBehavior = new SpawnByTeamBehavior(arena);

                    gameMode = new TeamDeathmatch(plugin, game, spawningBehavior, taskRunner, translator, viewFactory, config);
                    gameMode.addObjective(new EliminationObjective(game, 2));
                    gameMode.addObjective(new ScoreObjective(game, config.getKillsToWin()));
                    gameMode.addObjective(new TimeObjective(game, config.getTimeLimit()));

                    break;
                } catch (IOException e) {
                    throw new FactoryCreationException("Could not create gamemode of type \"" + gameModeType + "\"", e);
                }
            case ZOMBIES:
                try {
                    ZombiesConfig config = new ZombiesConfig(plugin.getDataFolder().getPath() + "/data/game_" + game.getId() + "/gamemodes", plugin.getResource("zombies.yml"), server);
                    PerkManager perkManager = new ZombiesPerkManager();
                    PowerUpManager powerUpManager = new ZombiesPowerUpManager(game, taskRunner);
                    SpawningBehavior spawningBehavior = new SpawnRandomlyBehavior(arena);

                    gameMode = new Zombies(plugin, game, translator, internals, spawningBehavior, perkManager, powerUpManager, taskRunner, viewFactory, config);
                    gameMode.addObjective(new EliminationObjective(game, 1));

                    break;
                } catch (IOException e) {
                    throw new FactoryCreationException("Could not create gamemode of type \"" + gameModeType + "\"", e);
                }
            default:
                throw new FactoryCreationException("Invalid game mode type \"" + gameModeType + "\"");
        }

        gameMode.onCreate();
        gameMode.loadData(arena);

        return gameMode;
    }
}

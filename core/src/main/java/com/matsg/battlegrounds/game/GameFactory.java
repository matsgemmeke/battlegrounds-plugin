package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import com.matsg.battlegrounds.api.storage.LevelConfig;
import com.matsg.battlegrounds.api.storage.PlayerStorage;
import com.matsg.battlegrounds.game.state.WaitingState;
import com.matsg.battlegrounds.storage.BattleCacheYaml;

import java.io.IOException;

public class GameFactory {

    private Battlegrounds plugin;
    private TaskRunner taskRunner;

    public GameFactory(Battlegrounds plugin, TaskRunner taskRunner) {
        this.plugin = plugin;
        this.taskRunner = taskRunner;
    }

    public Game make(int id) {
        CacheYaml cache = plugin.getBattlegroundsCache();
        EventDispatcher eventDispatcher = plugin.getEventDispatcher();
        LevelConfig levelConfig = plugin.getLevelConfig();
        PlayerStorage playerStorage = plugin.getPlayerStorage();
        Translator translator = plugin.getTranslator();

        CacheYaml dataFile;
        GameState gameState = new WaitingState();
        ItemRegistry itemRegistry = new BattleItemRegistry();
        MobManager mobManager = new BattleMobManager(plugin.getBattlegroundsConfig());

        try {
            dataFile = new BattleCacheYaml("setup.yml", plugin.getDataFolder().getPath() + "/data/game_" + id, null, plugin.getServer());
        } catch (IOException e) {
            throw new FactoryCreationException(e.getMessage(), e);
        }

        return new BattleGame(
                id,
                cache,
                dataFile,
                eventDispatcher,
                gameState,
                itemRegistry,
                levelConfig,
                mobManager,
                playerStorage,
                taskRunner,
                translator
        );
    }
}

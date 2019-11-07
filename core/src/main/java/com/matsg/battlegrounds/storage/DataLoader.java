package com.matsg.battlegrounds.storage;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.game.*;
import com.matsg.battlegrounds.mode.GameModeFactory;
import com.matsg.battlegrounds.mode.GameModeType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Random;
import java.util.logging.Logger;

public class DataLoader {

    private final Battlegrounds plugin;
    private final InternalsProvider internals;
    private final Logger logger;
    private final TaskRunner taskRunner;
    private final Translator translator;

    public DataLoader(Battlegrounds plugin, InternalsProvider internals, TaskRunner taskRunner, Translator translator) {
        this.plugin = plugin;
        this.internals = internals;
        this.taskRunner = taskRunner;
        this.translator = translator;
        this.logger = plugin.getLogger();

        plugin.getGameManager().getGames().clear();

        load();
    }

    private void load() {
        logger.info("Loading in games and arenas...");

        GameFactory gameFactory = new GameFactory(plugin, taskRunner);
        GameModeFactory gameModeFactory = new GameModeFactory(plugin, internals, taskRunner, translator);

        // Look for games files that have been created already
        try {
            File[] files = new File(plugin.getDataFolder().getPath() + "/data").listFiles();

            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.isDirectory() && file.getName().startsWith("game_")) {
                        int id = Integer.parseInt(file.getName().substring(5));

                        Game game = gameFactory.make(id);

                        plugin.getGameManager().getGames().add(game);
                    }
                }
            } else {
                logger.info("No games have been found!");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add existing arenas to the game
        try {
            for (Game game : plugin.getGameManager().getGames()) {
                int id = game.getId();
                CacheYaml data = game.getDataFile();
                ConfigurationSection arenaSection = data.getConfigurationSection("arena");

                if (arenaSection == null) {
                    logger.warning("No arenas were found for game " + id);
                    continue;
                }

                for (String name : arenaSection.getKeys(false)) {
                    logger.info("Adding arena " + name + " to game " + id);

                    Location max = null;
                    Location min = null;
                    World world = plugin.getServer().getWorld(data.getString("arena." + name + ".world"));

                    if (data.getString("arena." + name + ".max") != null) {
                        max = data.getLocation("arena." + name + ".max");
                        min = data.getLocation("arena." + name + ".min");
                    }

                    Arena arena = new BattleArena(name, world, max, min);

                    addComponents(game, arena);

                    ConfigurationSection spawnSection = arenaSection.getConfigurationSection(name + ".spawn");

                    if (spawnSection != null) {
                        for (String spawnIndex : spawnSection.getKeys(false)) {
                            Spawn spawn = new ArenaSpawn(Integer.parseInt(spawnIndex), data.getLocation("arena." + name + ".spawn." + spawnIndex + ".location"), spawnSection.getInt(spawnIndex + ".team"));
                            spawn.setTeamBase(spawnSection.getBoolean(spawnIndex + ".base"));
                            if (spawn.getLocation() != null) {
                                arena.getSpawnContainer().add(spawn);
                            }
                        }
                    }

                    game.getArenaList().add(arena);

                    logger.info("Succesfully added arena " + arena.getName() + " to game " + id);
                }

                // Assign an arena to this game
                game.setArena(game.getArenaList().get(new Random().nextInt(game.getArenaList().size())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Setting configurations
        try {
            for (Game game : plugin.getGameManager().getGames()) {
                ConfigurationSection config = game.getDataFile().getConfigurationSection("config");
                GameConfiguration configuration = new BattleGameConfiguration(
                        config.getStringList("gamemodes"),
                        config.getInt("maxplayers"),
                        config.getInt("minplayers"),
                        config.getInt("lobbycountdown")
                );

                for (String gameModeType : config.getStringList("gamemodes")) {
                    try {
                        GameMode gameMode = gameModeFactory.make(game, GameModeType.valueOf(gameModeType.toUpperCase()));

                        game.getGameModeList().add(gameMode);
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().severe("Invalid gamemode type \"" + gameModeType + "\"");
                    }
                }

                GameMode gameMode = game.getGameModeList().get(new Random().nextInt(game.getGameModeList().size()));
                gameMode.onCreate();

                game.setConfiguration(configuration);
                game.setGameMode(gameMode);
                gameMode.loadData(game.getArena());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set the game joining signs
        try {
            for (Game game : plugin.getGameManager().getGames()) {
                int id = game.getId();

                if (game.getDataFile().getString("sign") == null) {
                    logger.warning("No join sign was found for game " + id);
                    continue;
                }

                Location location = game.getDataFile().getLocation("sign");
                BlockState state = location.getBlock().getState();

                if (!(state instanceof Sign)) {
                    logger.warning("The sign of game " + id + " was corrupted!");
                    continue;
                }

                GameSign sign = new BattleGameSign(game, (Sign) state, translator, plugin.getBattlegroundsConfig());

                game.setGameSign(sign);
                sign.update();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("Loaded " + plugin.getGameManager().getGames().size() + " game(s) from the cache");
    }

    private void addComponents(Game game, Arena arena) {
        CacheYaml data = game.getDataFile();
        ConfigurationSection configurationSection = data.getConfigurationSection("arena." + arena.getName() + ".component");

        if (configurationSection == null) {
            logger.info("No components found for arena " + arena.getName() + "!");
            return;
        }

        for (String componentId : configurationSection.getKeys(false)) {
            String type = configurationSection.getString(componentId + ".type");

            if (type.equals("spawn")) {
                Location location = data.getLocation("arena." + arena.getName() + ".component." + componentId + ".location");
                boolean teamBase = configurationSection.getBoolean(componentId + ".base");
                int teamId = configurationSection.getInt(componentId + ".team");

                Spawn spawn = new ArenaSpawn(Integer.parseInt(componentId), location, teamId);
                spawn.setTeamBase(teamBase);

                arena.getSpawnContainer().add(spawn);
            }
        }
    }
}

package com.matsg.battlegrounds.config;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.CacheYaml;
import com.matsg.battlegrounds.api.config.WeaponConfig;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.game.*;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

public class DataLoader {

    private final Battlegrounds plugin;
    private final Logger logger;

    public DataLoader(Battlegrounds plugin) {
        this.logger = plugin.getLogger();
        this.plugin = plugin;

        plugin.getGameManager().getGames().clear();

        load();
    }

    private void load() {
        logger.info("Loading in games and arenas...");

        // Look for games files that have been created already
        try {
            File[] files = new File(plugin.getDataFolder().getPath() + "/data").listFiles();

            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (!file.isDirectory() && file.getName().startsWith("game_")) {
                        int id = Integer.parseInt(file.getName().substring(5, file.getName().length() - 4));

                        plugin.getGameManager().getGames().add(new BattleGame(plugin, id));
                    }
                }
            } else {
                logger.info("No games have been found!");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Setting configurations
        try {
            for (Game game : plugin.getGameManager().getGames()) {
                ConfigurationSection config = game.getDataFile().getConfigurationSection("_config");
                GameConfiguration configuration = new BattleGameConfiguration(
                        new GameMode[0],
                        config.getInt("maxplayers"),
                        config.getInt("minplayers"),
                        config.getInt("countdown")
                );

                game.setConfiguration(configuration);
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

                    Location max = data.getLocation("arena." + name + ".max"), min = data.getLocation("arena." + name + ".min");

                    if (max == null || min == null) {
                        logger.warning("Arena " + name + " could not be loaded because of invalid border locations");
                        continue;
                    }

                    Arena arena = new BattleArena(name, max, min, max.getWorld());
                    ConfigurationSection spawnSection = arenaSection.getConfigurationSection(name + ".spawn");

                    if (spawnSection != null) {
                        for (String spawnIndex : spawnSection.getKeys(false)) {
                            Spawn spawn = new ArenaSpawn(data.getLocation("arena." + name + ".spawn." + spawnIndex));
                            if (spawn.getLocation() != null) {
                                arena.getSpawns().add(spawn);
                            }
                        }
                    }

                    game.getArenaList().add(arena);

                    logger.info("Succesfully added arena " + name + " to game " + id);
                }

                // Assign an arena to this game
                game.setArena(game.getArenaList().get(new Random().nextInt(game.getArenaList().size())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set the game joining signs
        try {
            for (Game game : plugin.getGameManager().getGames()) {
                int id = game.getId();
                Location location = game.getDataFile().getLocation("sign");

                if (location == null) {
                    logger.warning("No join sign was found for game " + id);
                    continue;
                }

                BlockState state = location.getBlock().getState();

                if (!(state instanceof Sign)) {
                    logger.warning("The sign of game " + id + " was corrupted!");
                    continue;
                }

                GameSign sign = new BattleGameSign(plugin, game, (Sign) state);

                game.setGameSign(sign);
                sign.update();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("Loaded " + plugin.getGameManager().getGames().size() + " game(s) from the cache");
    }

    private Weapon getWeapon(String name) {
        for (WeaponConfig weaponConfig : Arrays.asList(plugin.getExplosiveConfig(), plugin.getFireArmConfig(), plugin.getKnifeConfig())) {
            Weapon weapon = weaponConfig.get(name);
            if (weapon != null) {
                return weapon;
            }
        }
        return null;
    }
}
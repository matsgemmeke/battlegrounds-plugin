package com.matsg.battlegrounds.storage;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.game.*;
import com.matsg.battlegrounds.game.component.*;
import com.matsg.battlegrounds.game.mode.GameModeFactory;
import com.matsg.battlegrounds.game.mode.GameModeType;
import com.matsg.battlegrounds.item.ItemFinder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class DataLoader {

    private final Battlegrounds plugin;
    private final Logger logger;
    private ItemFinder itemFinder;

    public DataLoader(Battlegrounds plugin) {
        this.plugin = plugin;
        this.itemFinder = new ItemFinder(plugin);
        this.logger = plugin.getLogger();

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
                    if (file.isDirectory() && file.getName().startsWith("game_")) {
                        int id = Integer.parseInt(file.getName().substring(5, file.getName().length()));

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

        GameModeFactory gameModeFactory = new GameModeFactory();

        // Setting configurations
        try {
            for (Game game : plugin.getGameManager().getGames()) {
                ConfigurationSection config = game.getDataFile().getConfigurationSection("config");
                List<GameMode> gameModes = new ArrayList<>();

                for (String gameModeType : config.getStringList("gamemodes")) {
                    GameMode gameMode;
                    try {
                        gameMode = gameModeFactory.make(game, GameModeType.valueOf(gameModeType.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().severe("Invalid gamemode type \"" + gameModeType + "\"");
                        continue;
                    }
                    gameModes.add(gameMode);
                }

                GameConfiguration configuration = new BattleGameConfiguration(
                        gameModes.toArray(new GameMode[gameModes.size()]),
                        config.getInt("maxplayers"),
                        config.getInt("minplayers"),
                        config.getInt("gamecountdown"),
                        config.getInt("lobbycountdown")
                );

                game.setConfiguration(configuration);
                game.setGameMode(game.getConfiguration().getGameModes()[new Random().nextInt(game.getConfiguration().getGameModes().length)]);
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
                    World world = plugin.getServer().getWorld(data.getString("arena." + name + ".world"));

                    Arena arena = new BattleArena(name, world, max, min);

                    addSections(game, arena);

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

    private void addSections(Game game, Arena arena) {
        CacheYaml data = game.getDataFile();
        ConfigurationSection configurationSection = data.getConfigurationSection("arena." + arena.getName() + ".component");

        if (configurationSection == null) {
            logger.info("No components found for arena " + arena.getName() + "!");
            return;
        }

        // Make sure the sections are loaded before the other components are being added.
        for (String componentId : configurationSection.getKeys(false)) {
            String type = configurationSection.getString(componentId + ".type");

            if (type.equals("section")) {
                String name = data.getString("arena." + arena.getName() + ".component." + componentId + ".name");
                int price = configurationSection.getInt(componentId + ".price");

                Section section = new ArenaSection(Integer.parseInt(componentId), name);
                section.setPrice(price);

                arena.getSectionContainer().add(section);

                logger.info("Added section " + section.getName());
            }
        }

        for (String componentId : configurationSection.getKeys(false)) {
            String type = configurationSection.getString(componentId + ".type");

            if (type.equals("door")) {
                String locationPath = "arena." + arena.getName() + ".component." + componentId;
                Location max = data.getLocation(locationPath + ".max");
                Location min = data.getLocation(locationPath + ".min");
                Material material = Material.valueOf(configurationSection.getString(componentId + ".material"));
                Section section = arena.getSection(configurationSection.getString(componentId + ".section"));

                Door door = new ArenaDoor(
                        Integer.parseInt(componentId),
                        game,
                        section,
                        arena.getWorld(),
                        max,
                        min,
                        material
                );

                section.getDoorContainer().add(door);
            }

            if (type.equals("itemchest")) {
                Location location = data.getLocation("arena." + arena.getName() + ".component." + componentId + ".location");
                Chest chest = (Chest) location.getBlock().getState();
                int price = configurationSection.getInt(componentId + ".price");
                Section section = arena.getSection(configurationSection.getString(componentId + ".section"));
                Weapon weapon = itemFinder.findWeapon(configurationSection.getString(componentId + ".item"));

                ItemChest itemChest = new ArenaItemChest(
                        Integer.parseInt(componentId),
                        chest,
                        weapon,
                        weapon.getName(),
                        weapon.getItemStack(),
                        price
                );

                section.getItemChestContainer().add(itemChest);
            }

            if (type.equals("mobspawn")) {
                Location location = data.getLocation("arena." + arena.getName() + ".component." + componentId + ".location");
                Section section = arena.getSection(configurationSection.getString(componentId + ".section"));

                MobSpawn mobSpawn = new ArenaMobSpawn(Integer.parseInt(componentId), location);

                section.getMobSpawnContainer().add(mobSpawn);
            }

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

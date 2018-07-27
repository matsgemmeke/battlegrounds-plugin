package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.*;
import com.matsg.battlegrounds.api.config.BattlegroundsConfig;
import com.matsg.battlegrounds.api.config.CacheYaml;
import com.matsg.battlegrounds.api.config.LevelConfig;
import com.matsg.battlegrounds.api.config.WeaponConfig;
import com.matsg.battlegrounds.api.item.Equipment;
import com.matsg.battlegrounds.api.item.FireArm;
import com.matsg.battlegrounds.api.item.Knife;
import com.matsg.battlegrounds.api.player.PlayerStorage;
import com.matsg.battlegrounds.command.BattlegroundsCommand;
import com.matsg.battlegrounds.command.LoadoutCommand;
import com.matsg.battlegrounds.config.*;
import com.matsg.battlegrounds.listener.EventListener;
import com.matsg.battlegrounds.listener.PlayerSwapItemListener;
import com.matsg.battlegrounds.player.LocalPlayerStorage;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BattlegroundsPlugin extends JavaPlugin implements Battlegrounds {

    private static Battlegrounds plugin;
    private BattlegroundsConfig config;
    private CacheYaml cache;
    private GameManager gameManager;
    private LevelConfig levelConfig;
    private List<BattlegroundsExtension> extensions;
    private PlayerStorage playerStorage;
    private Translator translator;
    private WeaponConfig<Equipment> equipmentConfig;
    private WeaponConfig<FireArm> fireArmConfig;
    private WeaponConfig<Knife> knifeConfig;

    public void onEnable() {
        plugin = this;

        try {
            startPlugin();
        } catch (StartupFailedException e) {
            getLogger().severe("An error occurred while enabling Battlegrounds v" + getDescription().getVersion());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("Succesfully started Battlegrounds (" + extensions.size() + " extensions loaded)");
    }

    public void onDisable() {
        gameManager.shutdown();
    }

    public static Battlegrounds getPlugin() {
        return plugin;
    }

    public static WorldEditPlugin getWorldEditPlugin() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
            return null;
        }
        return (WorldEditPlugin) plugin;
    }

    public CacheYaml getBattlegroundsCache() {
        return cache;
    }

    public BattlegroundsConfig getBattlegroundsConfig() {
        return config;
    }

    public WeaponConfig<Equipment> getEquipmentConfig() {
        return equipmentConfig;
    }

    public WeaponConfig<FireArm> getFireArmConfig() {
        return fireArmConfig;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public WeaponConfig<Knife> getKnifeConfig() {
        return knifeConfig;
    }

    public LevelConfig getLevelConfig() {
        return levelConfig;
    }

    public PlayerStorage getPlayerStorage() {
        return playerStorage;
    }

    public Translator getTranslator() {
        return translator;
    }

    public boolean loadConfigs() {
        try {
            cache = new BattleCacheYaml(this, "cache.yml");
            config = new BattlegroundsConfig(this);
            equipmentConfig = new EquipmentConfig(this);
            fireArmConfig = new FireArmConfig(this);
            knifeConfig = new KnifeConfig(this);
            translator = new PluginTranslator(this);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private List<BattlegroundsExtension> loadExtensions() {
        List<BattlegroundsExtension> list = new ArrayList<>();
        for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
            if (plugin instanceof BattlegroundsExtension) {
                list.add((BattlegroundsExtension) plugin);
            }
        }
        return list;
    }

    private void startPlugin() throws StartupFailedException {
        extensions = loadExtensions();

        try {
            cache = new BattleCacheYaml(this, "cache.yml");
            config = new BattlegroundsConfig(this);
        } catch (Exception e) {
            throw new StartupFailedException("Failed to load configuration files!", e);
        }

        try {
            translator = new PluginTranslator(this);
        } catch (Exception e) {
            throw new StartupFailedException("Failed to initialize the translator!", e);
        }

        try {
            equipmentConfig = new EquipmentConfig(this);
            fireArmConfig = new FireArmConfig(this);
            knifeConfig = new KnifeConfig(this);
        } catch (Exception e) {
            throw new StartupFailedException("Failed to load weapon configuration files!", e);
        }

        try {
            levelConfig = new BattleLevelConfig(this);
        } catch (Exception e) {
            throw new StartupFailedException("Failed to load the level configuration!", e);
        }

        try {
            playerStorage = new LocalPlayerStorage(this);
        } catch (Exception e) {
            throw new StartupFailedException("Failed to load default loadout classes!", e);
        }

        getLogger().info("Succesfully loaded " + fireArmConfig.getList().size() + " guns, "
                + equipmentConfig.getList().size() + " equipment and "
                + knifeConfig.getList().size() + " knives from the config");

        this.gameManager = new BattleGameManager();

        new DataLoader(this);

        new BattlegroundsCommand(this);
        new LoadoutCommand(this);

        new EventListener(this);
        new PlayerSwapItemListener(this);

        for (BattlegroundsExtension extension : extensions) {
            extension.onInit();
        }
    }
}
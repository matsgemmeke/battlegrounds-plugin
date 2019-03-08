package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.*;
import com.matsg.battlegrounds.api.config.BattlegroundsConfig;
import com.matsg.battlegrounds.api.config.CacheYaml;
import com.matsg.battlegrounds.api.config.ItemConfig;
import com.matsg.battlegrounds.api.config.LevelConfig;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.player.PlayerStorage;
import com.matsg.battlegrounds.command.BattlegroundsCommand;
import com.matsg.battlegrounds.command.LoadoutCommand;
import com.matsg.battlegrounds.config.*;
import com.matsg.battlegrounds.event.EventListener;
import com.matsg.battlegrounds.event.PlayerSwapItemListener;
import com.matsg.battlegrounds.item.factory.AttachmentFactory;
import com.matsg.battlegrounds.item.factory.EquipmentFactory;
import com.matsg.battlegrounds.item.factory.FirearmFactory;
import com.matsg.battlegrounds.item.factory.MeleeWeaponFactory;
import com.matsg.battlegrounds.nms.ReflectionUtils;
import com.matsg.battlegrounds.nms.VersionManager;
import com.matsg.battlegrounds.player.LocalPlayerStorage;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class BattlegroundsPlugin extends JavaPlugin implements Battlegrounds {

    private static Battlegrounds plugin;
    private BattlegroundsConfig config;
    private CacheYaml cache;
    private EventManager eventManager;
    private GameManager gameManager;
    private ItemFactory<Attachment> attachmentFactory;
    private ItemFactory<Equipment> equipmentFactory;
    private ItemFactory<Firearm> firearmFactory;
    private ItemFactory<MeleeWeapon> meleeWeaponFactory;
    private LevelConfig levelConfig;
    private PlayerStorage playerStorage;
    private VersionManager versionManager;

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

        getLogger().info("Succesfully started Battlegrounds " + getDescription().getVersion());
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

    public ItemFactory<Attachment> getAttachmentFactory() {
        return attachmentFactory;
    }

    public CacheYaml getBattlegroundsCache() {
        return cache;
    }

    public BattlegroundsConfig getBattlegroundsConfig() {
        return config;
    }

    public ItemFactory<Equipment> getEquipmentFactory() {
        return equipmentFactory;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public ItemFactory<Firearm> getFirearmFactory() {
        return firearmFactory;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public LevelConfig getLevelConfig() {
        return levelConfig;
    }

    public ItemFactory<MeleeWeapon> getMeleeWeaponFactory() {
        return meleeWeaponFactory;
    }

    public PlayerStorage getPlayerStorage() {
        return playerStorage;
    }

    public Version getVersion() {
        return versionManager.getVersion();
    }

    public boolean loadConfigs() {
        try {
            cache = new BattleCacheYaml(this, "cache.yml");
            config = new BattlegroundsConfig(this);

            ItemConfig attachmentConfig = new AttachmentConfig(this);
            ItemConfig equipmentConfig = new EquipmentConfig(this);
            ItemConfig firearmConfig = new FirearmConfig(this);
            ItemConfig meleeWeaponConfig = new MeleeWeaponConfig(this);

            attachmentFactory = new AttachmentFactory(attachmentConfig);
            equipmentFactory = new EquipmentFactory(equipmentConfig);
            firearmFactory = new FirearmFactory(firearmConfig);
            meleeWeaponFactory = new MeleeWeaponFactory(meleeWeaponConfig);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private void startPlugin() throws StartupFailedException {
        try {
            cache = new BattleCacheYaml(this, "cache.yml");
            config = new BattlegroundsConfig(this);
        } catch (Exception e) {
            throw new StartupFailedException("Failed to load configuration files!", e);
        }

        Translator.setLanguageDirectory(new File(getDataFolder().getPath() + "/lang"));
        Translator.setLocale(Locale.ENGLISH.getLanguage()); // TODO: Multi language support

        try {
            ItemConfig attachmentConfig = new AttachmentConfig(this);
            ItemConfig equipmentConfig = new EquipmentConfig(this);
            ItemConfig firearmConfig = new FirearmConfig(this);
            ItemConfig meleeWeaponConfig = new MeleeWeaponConfig(this);

            attachmentFactory = new AttachmentFactory(attachmentConfig);
            equipmentFactory = new EquipmentFactory(equipmentConfig);
            firearmFactory = new FirearmFactory(firearmConfig);
            meleeWeaponFactory = new MeleeWeaponFactory(meleeWeaponConfig);
        } catch (Exception e) {
            throw new StartupFailedException("Failed to load item configuration files!", e);
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

        eventManager = new BattleEventManager();
        gameManager = new BattleGameManager();
        versionManager = new VersionManager();

        new DataLoader(this);

        new BattlegroundsCommand(this);
        new LoadoutCommand(this);

        new EventListener(this);

        if (ReflectionUtils.getEnumVersion().getValue() > 8) {
            new PlayerSwapItemListener(this);
        }
    }
}

package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.*;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.storage.*;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.command.BattlegroundsCommand;
import com.matsg.battlegrounds.command.Command;
import com.matsg.battlegrounds.command.LoadoutCommand;
import com.matsg.battlegrounds.event.BattleEventDispatcher;
import com.matsg.battlegrounds.nms.VersionFactory;
import com.matsg.battlegrounds.storage.*;
import com.matsg.battlegrounds.event.EventListener;
import com.matsg.battlegrounds.event.PlayerSwapItemListener;
import com.matsg.battlegrounds.item.factory.AttachmentFactory;
import com.matsg.battlegrounds.item.factory.EquipmentFactory;
import com.matsg.battlegrounds.item.factory.FirearmFactory;
import com.matsg.battlegrounds.item.factory.MeleeWeaponFactory;
import com.matsg.battlegrounds.nms.ReflectionUtils;
import com.matsg.battlegrounds.storage.local.LocalPlayerStorage;
import com.matsg.battlegrounds.storage.sql.SQLPlayerStorage;
import org.apache.commons.lang.LocaleUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BattlegroundsPlugin extends JavaPlugin implements Battlegrounds {

    private static Battlegrounds plugin;
    private BattlegroundsConfig config;
    private CacheYaml cache;
    private EventDispatcher eventDispatcher;
    private GameManager gameManager;
    private ItemFactory<Attachment> attachmentFactory;
    private ItemFactory<Equipment> equipmentFactory;
    private ItemFactory<Firearm> firearmFactory;
    private ItemFactory<MeleeWeapon> meleeWeaponFactory;
    private LevelConfig levelConfig;
    private SelectionManager selectionManager;
    private PlayerStorage playerStorage;
    private Translator translator;
    private Version version;

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
        if (gameManager != null) {
            gameManager.shutdown();
        }
    }

    public static Battlegrounds getPlugin() {
        return plugin;
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

    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
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

    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    public Translator getTranslator() {
        return translator;
    }

    public Version getVersion() {
        return version;
    }

    public boolean loadConfigs() {
        try {
            cache = new BattleCacheYaml(this, "cache.yml");
            config = new BattlegroundsConfig(this);

            ItemConfig attachmentConfig = new AttachmentConfig(this);
            ItemConfig equipmentConfig = new EquipmentConfig(this);
            ItemConfig firearmConfig = new FirearmConfig(this);
            ItemConfig meleeWeaponConfig = new MeleeWeaponConfig(this);

            attachmentFactory = new AttachmentFactory(this, attachmentConfig);
            equipmentFactory = new EquipmentFactory(this, equipmentConfig);
            firearmFactory = new FirearmFactory(this, firearmConfig);
            meleeWeaponFactory = new MeleeWeaponFactory(this, meleeWeaponConfig);
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

        setUpTranslator();
        setUpCommands();

        try {
            ItemConfig attachmentConfig = new AttachmentConfig(this);
            ItemConfig equipmentConfig = new EquipmentConfig(this);
            ItemConfig firearmConfig = new FirearmConfig(this);
            ItemConfig meleeWeaponConfig = new MeleeWeaponConfig(this);

            attachmentFactory = new AttachmentFactory(this, attachmentConfig);
            equipmentFactory = new EquipmentFactory(this, equipmentConfig);
            firearmFactory = new FirearmFactory(this, firearmConfig);
            meleeWeaponFactory = new MeleeWeaponFactory(this, meleeWeaponConfig);
        } catch (Exception e) {
            throw new StartupFailedException("Failed to load item configuration files!", e);
        }

        try {
            levelConfig = new BattleLevelConfig(this);
        } catch (Exception e) {
            throw new StartupFailedException("Failed to load the level configuration!", e);
        }

        try {
            SQLConfig sqlConfig = new SQLConfig(this);

            if (sqlConfig.isEnabled()) {
                playerStorage = new SQLPlayerStorage(this, sqlConfig);
            } else {
                playerStorage = new LocalPlayerStorage(this);
            }
        } catch (IOException e) {
            throw new StartupFailedException("Failed to set up player storage!", e);
        }

        VersionFactory versionFactory = new VersionFactory();
        version = versionFactory.make(ReflectionUtils.getEnumVersion());
        version.registerCustomEntities();

        eventDispatcher = new BattleEventDispatcher();
        gameManager = new BattleGameManager();
        selectionManager = new BattleSelectionManager();

        new DataLoader(this, translator, version);

        new EventListener(this);

        if (ReflectionUtils.getEnumVersion().getValue() > 8) {
            new PlayerSwapItemListener(this);
        }
    }

    private void setUpCommands() {
        List<Command> commands = new ArrayList<>();
        commands.add(new BattlegroundsCommand(this));
        commands.add(new LoadoutCommand(this));

        for (Command command : commands) {
            getCommand(command.getName()).setExecutor(command);

            for (String alias : command.getAliases()) {
                getCommand(alias).setExecutor(command);
            }
        }
    }

    private void setUpTranslator() {
        translator = new PluginTranslator();
        translator.setLocale(Locale.ENGLISH);

        File languageDirectory = new File(getDataFolder().getPath() + "/lang");

        try {
            // Get all available language files and loop and add them to the translator
            File[] files = languageDirectory.listFiles();

            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (!file.isDirectory() && file.getName().startsWith("lang_")) {
                        Locale locale;

                        try {
                            locale = LocaleUtils.toLocale(file.getName().substring(5, file.getName().length() - 4));
                        } catch (NumberFormatException e) {
                            getLogger().severe("Could not read language file " + file.getName() + ": " + e.getMessage());
                            continue;
                        }

                        Yaml yaml = new BattleCacheYaml(this, languageDirectory.getPath(), "lang_" + locale.getLanguage() + ".yml");
                        LanguageConfiguration languageConfiguration = new LanguageConfiguration(locale, yaml);

                        translator.getLanguageConfigurations().add(languageConfiguration);
                    }
                }
            } else {
                // Generate a new default language file
                Yaml yaml = new BattleCacheYaml(this, languageDirectory.getPath(), "lang_en.yml");
                LanguageConfiguration languageConfiguration = new LanguageConfiguration(Locale.ENGLISH, yaml);

                translator.getLanguageConfigurations().add(languageConfiguration);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

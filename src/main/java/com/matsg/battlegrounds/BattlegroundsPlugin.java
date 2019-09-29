package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.*;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.storage.*;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.command.*;
import com.matsg.battlegrounds.event.BattleEventDispatcher;
import com.matsg.battlegrounds.game.GameFactory;
import com.matsg.battlegrounds.item.factory.*;
import com.matsg.battlegrounds.nms.VersionFactory;
import com.matsg.battlegrounds.storage.*;
import com.matsg.battlegrounds.event.EventListener;
import com.matsg.battlegrounds.event.PlayerSwapItemListener;
import com.matsg.battlegrounds.nms.ReflectionUtils;
import com.matsg.battlegrounds.storage.local.LocalPlayerStorage;
import com.matsg.battlegrounds.storage.sql.SQLPlayerStorage;
import org.apache.commons.lang.LocaleUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

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
    private TaskRunner taskRunner;
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
            String cacheFileName = "cache.yml";

            cache = new BattleCacheYaml(cacheFileName, getDataFolder().getPath(), getResource(cacheFileName), getServer());
            config = new BattlegroundsConfig(getDataFolder().getPath(), getResource("config.yml"));

            // Auto-update the config if the plugin was updated
            if (!config.getString("version").equals(getDescription().getVersion())) {
                config.removeFile();
                config = new BattlegroundsConfig(getDataFolder().getPath(), getResource("config.yml"));
            }

            String filePath = getDataFolder().getPath() + "/items";
            ItemConfig attachmentConfig = new AttachmentConfig(filePath, getResource("attachments.yml"));
            ItemConfig equipmentConfig = new EquipmentConfig(filePath, getResource("equipment.yml"));
            ItemConfig firearmConfig = new FirearmConfig(filePath, getResource("guns.yml"));
            ItemConfig meleeWeaponConfig = new MeleeWeaponConfig(filePath, getResource("melee_weapons.yml"));

            FireModeFactory fireModeFactory = new FireModeFactory(taskRunner);
            IgnitionSystemFactory ignitionSystemFactory = new IgnitionSystemFactory(taskRunner);
            LaunchSystemFactory launchSystemFactory = new LaunchSystemFactory(taskRunner, version);
            ReloadSystemFactory reloadSystemFactory = new ReloadSystemFactory();
            TacticalEffectFactory tacticalEffectFactory = new TacticalEffectFactory(taskRunner);

            attachmentFactory = new AttachmentFactory(attachmentConfig, fireModeFactory, reloadSystemFactory);
            equipmentFactory = new EquipmentFactory(equipmentConfig, eventDispatcher, ignitionSystemFactory, tacticalEffectFactory, taskRunner, translator, version);
            firearmFactory = new FirearmFactory(firearmConfig, eventDispatcher, fireModeFactory, launchSystemFactory, reloadSystemFactory, taskRunner, translator, version, config);
            meleeWeaponFactory = new MeleeWeaponFactory(meleeWeaponConfig, eventDispatcher, taskRunner, translator, version);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private void startPlugin() throws StartupFailedException {
        setUpTranslator();

        taskRunner = new TaskRunner() {
            public BukkitTask runTaskLater(BukkitRunnable runnable, long delay) {
                return runnable.runTaskLater(plugin, delay);
            }

            public BukkitTask runTaskLater(Runnable runnable, long delay) {
                return getServer().getScheduler().runTaskLater(plugin, runnable, delay);
            }

            public BukkitTask runTaskTimer(BukkitRunnable runnable, long delay, long period) {
                return runnable.runTaskTimer(plugin, delay, period);
            }

            public BukkitTask runTaskTimer(Runnable runnable, long delay, long period) {
                return getServer().getScheduler().runTaskTimer(plugin, runnable, delay, period);
            }
        };

        VersionFactory versionFactory = new VersionFactory();
        version = versionFactory.make(ReflectionUtils.getEnumVersion());
        version.registerCustomEntities();

        eventDispatcher = new BattleEventDispatcher(getServer().getPluginManager());
        gameManager = new BattleGameManager();
        selectionManager = new BattleSelectionManager();

        if (!loadConfigs()) {
            throw new StartupFailedException("Failed to load item configuration files!");
        }

        try {
            levelConfig = new BattleLevelConfig(getDataFolder().getPath(), getResource("levels.yml"));
        } catch (Exception e) {
            throw new StartupFailedException("Failed to load the level configuration!", e);
        }

        try {
            DefaultLoadouts defaultLoadouts = new DefaultLoadouts(getDataFolder().getPath(), getResource("default_loadouts.yml"), firearmFactory, equipmentFactory, meleeWeaponFactory);
            SQLConfig sqlConfig = new SQLConfig(getDataFolder().getPath(), getResource("sql.yml"));

            if (sqlConfig.isEnabled()) {
                playerStorage = new SQLPlayerStorage(sqlConfig, defaultLoadouts);
            } else {
                File playersDirectory = new File(getDataFolder().getPath() + "/players");

                playerStorage = new LocalPlayerStorage(playersDirectory, defaultLoadouts);
            }
        } catch (IOException e) {
            throw new StartupFailedException("Failed to set up player storage!", e);
        }

        new EventListener(this);

        new DataLoader(this, taskRunner, translator, version);

        if (ReflectionUtils.getEnumVersion().getValue() > 8) {
            new PlayerSwapItemListener(this);
        }

        setUpCommands();
    }

    private void setUpCommands() {
        List<Command> commands = new ArrayList<>();
        GameFactory gameFactory = new GameFactory(this, taskRunner);
        ItemFinder itemFinder = new ItemFinder(this);

        Command bgCommand = new BattlegroundsCommand(translator);
        bgCommand.addSubCommand(new AddComponent(translator, gameManager, itemFinder, selectionManager));
        bgCommand.addSubCommand(new CreateArena(translator, gameManager, selectionManager));
        bgCommand.addSubCommand(new CreateGame(translator, gameFactory, gameManager));
        bgCommand.addSubCommand(new Join(translator, gameManager, config));
        bgCommand.addSubCommand(new Leave(translator, gameManager));
        bgCommand.addSubCommand(new Overview(this, translator));
        bgCommand.addSubCommand(new Reload(this, translator));
        bgCommand.addSubCommand(new RemoveArena(translator, gameManager, taskRunner));
        bgCommand.addSubCommand(new RemoveComponent(translator, gameManager));
        bgCommand.addSubCommand(new RemoveGame(translator, gameManager, taskRunner));
        bgCommand.addSubCommand(new SetGameSign(translator, gameManager, config));
        bgCommand.addSubCommand(new SetLobby(translator, gameManager));
        bgCommand.addSubCommand(new SetMainLobby(translator, cache));
        bgCommand.addSubCommand(new Help(translator, bgCommand.getSubCommands(), version));

        Command loadoutCommand = new LoadoutCommand(this, translator, levelConfig, playerStorage, config.loadoutCreationLevel);
        loadoutCommand.addSubCommand(new Rename(translator, playerStorage));

        commands.add(bgCommand);

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

                        String fileName = "lang_" + locale.getLanguage() + ".yml";
                        Yaml yaml = new BattleCacheYaml(fileName, languageDirectory.getPath(), getResource(fileName), getServer());
                        LanguageConfiguration languageConfiguration = new LanguageConfiguration(locale, yaml);

                        translator.getLanguageConfigurations().add(languageConfiguration);
                    }
                }
            } else {
                // Generate a new default language file
                String fileName = "lang_en.yml";
                Yaml yaml = new BattleCacheYaml(fileName, languageDirectory.getPath(), getResource(fileName), getServer());
                LanguageConfiguration languageConfiguration = new LanguageConfiguration(Locale.ENGLISH, yaml);

                translator.getLanguageConfigurations().add(languageConfiguration);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

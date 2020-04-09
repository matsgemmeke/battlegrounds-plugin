package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.*;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.storage.*;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.command.*;
import com.matsg.battlegrounds.event.BattleEventDispatcher;
import com.matsg.battlegrounds.game.GameFactory;
import com.matsg.battlegrounds.gui.BattleViewFactory;
import com.matsg.battlegrounds.gui.ViewFactory;
import com.matsg.battlegrounds.item.factory.*;
import com.matsg.battlegrounds.mode.GameModeFactory;
import com.matsg.battlegrounds.storage.*;
import com.matsg.battlegrounds.event.EventListener;
import com.matsg.battlegrounds.event.PlayerSwapItemListener;
import com.matsg.battlegrounds.storage.sql.SQLConfig;
import com.matsg.battlegrounds.util.ReflectionUtils;
import com.matsg.battlegrounds.storage.local.LocalPlayerStorage;
import com.matsg.battlegrounds.storage.sql.SQLPlayerStorage;
import org.apache.commons.lang.LocaleUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BattlegroundsPlugin extends JavaPlugin implements Battlegrounds {

    private static Battlegrounds plugin;
    private BattlegroundsConfig config;
    private CacheYaml cache;
    private EventDispatcher eventDispatcher;
    private GameManager gameManager;
    private InternalsProvider internals;
    private ItemFactory<Attachment> attachmentFactory;
    private ItemFactory<Equipment> equipmentFactory;
    private ItemFactory<Firearm> firearmFactory;
    private ItemFactory<MeleeWeapon> meleeWeaponFactory;
    private ItemFinder itemFinder;
    private LevelConfig levelConfig;
    private SelectionManager selectionManager;
    private PlayerStorage playerStorage;
    private TaskRunner taskRunner;
    private Translator translator;
    private ViewFactory viewFactory;

    public void onEnable() {
        plugin = this;

        try {
            startPlugin();
        } catch (StartupFailedException e) {
            getLogger().severe("An error occurred while enabling Battlegrounds v" + getDescription().getVersion());
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
            ItemConfig attachmentConfig = new AttachmentConfig(filePath, getResource("items/attachments.yml"));
            ItemConfig equipmentConfig = new EquipmentConfig(filePath, getResource("items/equipment.yml"));
            ItemConfig firearmConfig = new FirearmConfig(filePath, getResource("items/guns.yml"));
            ItemConfig meleeWeaponConfig = new MeleeWeaponConfig(filePath, getResource("items/melee_weapons.yml"));

            FireModeFactory fireModeFactory = new FireModeFactory(taskRunner);
            IgnitionSystemFactory ignitionSystemFactory = new IgnitionSystemFactory(taskRunner);
            LaunchSystemFactory launchSystemFactory = new LaunchSystemFactory(internals, taskRunner);
            ReloadSystemFactory reloadSystemFactory = new ReloadSystemFactory();
            TacticalEffectFactory tacticalEffectFactory = new TacticalEffectFactory(taskRunner);

            attachmentFactory = new AttachmentFactory(attachmentConfig, fireModeFactory, reloadSystemFactory);
            equipmentFactory = new EquipmentFactory(equipmentConfig, eventDispatcher, ignitionSystemFactory, internals, tacticalEffectFactory, taskRunner, translator);
            firearmFactory = new FirearmFactory(firearmConfig, eventDispatcher, fireModeFactory, internals, launchSystemFactory, reloadSystemFactory, taskRunner, translator, config);
            meleeWeaponFactory = new MeleeWeaponFactory(meleeWeaponConfig, eventDispatcher, internals, taskRunner, translator);
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

        try {
            String packageName = BattlegroundsPlugin.class.getPackage().getName();
            String internalsName = getServer().getClass().getPackage().getName().split("\\.")[3];
            internals = (InternalsProvider) Class.forName(packageName + ".nms." + internalsName + "." + internalsName.toUpperCase()).newInstance();
            internals.registerCustomEntities();
        } catch (Exception e) {
            throw new StartupFailedException("Failed to find a valid implementation for this server version");
        }

        eventDispatcher = new BattleEventDispatcher(getServer().getPluginManager());
        gameManager = new BattleGameManager();
        itemFinder = new ItemFinder(this);
        selectionManager = new BattleSelectionManager();
        viewFactory = new BattleViewFactory(this, internals, itemFinder, taskRunner);

        if (!loadConfigs()) {
            throw new StartupFailedException("Failed to load item configuration files!");
        }

        try {
            levelConfig = new BattleLevelConfig(getDataFolder().getPath(), getResource("levels.yml"));
        } catch (Exception e) {
            throw new StartupFailedException("Failed to load the level configuration!", e);
        }

        // Generate gui layout files
        String layoutFolderPath = getDataFolder().getPath() + "/layout";
        File layoutFolder = new File(layoutFolderPath);

        if (!layoutFolder.exists()) {
            layoutFolder.mkdirs();

            try {
                Files.copy(getResource("layout/arena_settings.xml"), new File(layoutFolderPath + "/arena_settings.xml").toPath());
                Files.copy(getResource("layout/edit_game_configuration.xml"), new File(layoutFolderPath + "/edit_game_configuration.xml").toPath());
                Files.copy(getResource("layout/edit_loadout.xml"), new File(layoutFolderPath + "/edit_loadout.xml").toPath());
                Files.copy(getResource("layout/game_settings.xml"), new File(layoutFolderPath + "/game_settings.xml").toPath());
                Files.copy(getResource("layout/item_transaction.xml"), new File(layoutFolderPath + "/item_transaction.xml").toPath());
                Files.copy(getResource("layout/loadout_manager.xml"), new File(layoutFolderPath + "/loadout_manager.xml").toPath());
                Files.copy(getResource("layout/plugin_settings.xml"), new File(layoutFolderPath + "/plugin_settings.xml").toPath());
                Files.copy(getResource("layout/section_settings.xml"), new File(layoutFolderPath + "/section_settings.xml").toPath());
                Files.copy(getResource("layout/select_attachment.xml"), new File(layoutFolderPath + "/select_attachment.xml").toPath());
                Files.copy(getResource("layout/select_gamemodes.xml"), new File(layoutFolderPath + "/select_gamemodes.xml").toPath());
                Files.copy(getResource("layout/select_loadout.xml"), new File(layoutFolderPath + "/select_loadout.xml").toPath());
                Files.copy(getResource("layout/select_weapon.xml"), new File(layoutFolderPath + "/select_weapon.xml").toPath());
                Files.copy(getResource("layout/select_weapon_type.xml"), new File(layoutFolderPath + "/select_weapon_type.xml").toPath());
                Files.copy(getResource("layout/zombies_settings.xml"), new File(layoutFolderPath + "/zombies_settings.xml").toPath());
            } catch (IOException e) {
                throw new StartupFailedException("Failed to generate gui layout files!", e);
            }
        }

        // Setup a player storage based on configuration settings
        try {
            DefaultLoadouts defaultLoadouts = new DefaultLoadouts(getDataFolder().getPath(), getResource("default_loadouts.yml"), firearmFactory, equipmentFactory, meleeWeaponFactory);
            SQLConfig sqlConfig = new SQLConfig(getDataFolder().getPath(), getResource("sql_config.yml"));

            if (sqlConfig.isEnabled()) {
                playerStorage = new SQLPlayerStorage(sqlConfig, defaultLoadouts);
            } else {
                File playersDirectory = new File(getDataFolder().getPath() + "/players");

                playerStorage = new LocalPlayerStorage(playersDirectory, defaultLoadouts);
            }
        } catch (IOException e) {
            throw new StartupFailedException("Failed to set up player storage!", e);
        }

        new EventListener(this, eventDispatcher, internals, translator);

        new DataLoader(this, internals, taskRunner, translator, viewFactory);

        if (ReflectionUtils.getEnumVersion().getValue() > 8) {
            new PlayerSwapItemListener(this);
        }

        setUpCommands();
    }

    private void setUpCommands() {
        List<Command> commands = new ArrayList<>();
        GameFactory gameFactory = new GameFactory(this, taskRunner);
        GameModeFactory gameModeFactory = new GameModeFactory(this, internals, taskRunner, translator, viewFactory);

        Command bgCommand = new BattlegroundsCommand(translator);
        bgCommand.addSubCommand(new AddComponent(translator, gameManager, itemFinder, selectionManager));
        bgCommand.addSubCommand(new CreateArena(translator, gameManager, selectionManager));
        bgCommand.addSubCommand(new CreateGame(translator, gameFactory, gameManager, gameModeFactory));
        bgCommand.addSubCommand(new Join(translator, gameManager, config));
        bgCommand.addSubCommand(new Leave(translator, gameManager));
        bgCommand.addSubCommand(new Reload(this, translator));
        bgCommand.addSubCommand(new RemoveArena(translator, gameManager, taskRunner));
        bgCommand.addSubCommand(new RemoveComponent(translator, gameManager));
        bgCommand.addSubCommand(new RemoveGame(translator, gameManager, taskRunner));
        bgCommand.addSubCommand(new SetGameSign(translator, gameManager, config));
        bgCommand.addSubCommand(new SetLobby(translator, gameManager));
        bgCommand.addSubCommand(new SetMainLobby(translator, cache));
        bgCommand.addSubCommand(new Settings(translator, viewFactory));
        bgCommand.addSubCommand(new Wand(translator));
        bgCommand.addSubCommand(new Help(translator, bgCommand.getSubCommands(), internals));

        Command loadoutCommand = new LoadoutCommand(translator, levelConfig, playerStorage, viewFactory, config.loadoutCreationLevel);
        loadoutCommand.addSubCommand(new Rename(translator, playerStorage));

        commands.add(bgCommand);
        commands.add(loadoutCommand);

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

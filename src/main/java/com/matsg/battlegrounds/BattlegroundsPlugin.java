package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.config.BattlegroundsConfig;
import com.matsg.battlegrounds.api.config.CacheYaml;
import com.matsg.battlegrounds.api.config.WeaponConfig;
import com.matsg.battlegrounds.api.item.Explosive;
import com.matsg.battlegrounds.api.item.FireArm;
import com.matsg.battlegrounds.api.item.Knife;
import com.matsg.battlegrounds.config.BattlegroundsCacheYaml;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class BattlegroundsPlugin extends JavaPlugin implements Battlegrounds {

    private static BattlegroundsPlugin plugin;
    private BattlegroundsConfig config;
    private CacheYaml cache;
    //private ExplosiveConfig explosiveConfig;
    //private FireArmConfig fireArmConfig;
    private GameManager gameManager;
    //private KnifeConfig knifeConfig;
    //private PlayerData playerData;
    //private SQLConfig sqlConfig;
    private Translator translator;

    public void onEnable() {
        plugin = this;

        try {
            startPlugin();
        } catch (StartupFailedException e) {
            getLogger().severe("An error occurred while enabling Zombies v" + getDescription().getVersion());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("Succesfully started Zombies");
    }

    public void onDisable() {
        gameManager.shutdown();
    }

    public static BattlegroundsPlugin getPlugin() {
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

    public WeaponConfig<Explosive> getExplosiveConfig() {
        return null;
    }

    public WeaponConfig<FireArm> getFireArmConfig() {
        return null;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public WeaponConfig<Knife> getKnifeConfig() {
        return null;
    }

    public Translator getTranslator() {
        return translator;
    }

    public boolean loadConfigs() {
        try {
            cache = new BattlegroundsCacheYaml(this, "cache.yml");
            config = new BattlegroundsConfig(this);
            //explosiveConfig = new ExplosiveConfig(this);
            //fireArmConfig = new FireArmConfig(this);
            //knifeConfig = new KnifeConfig(this);
            //playerData = new PlayerData(this);
            //sqlConfig = new SQLConfig(this);
            translator = new PluginTranslator(this);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private void startPlugin() throws StartupFailedException {
        try {
            cache = new BattlegroundsCacheYaml(this, "cache.yml");
            config = new BattlegroundsConfig(this);
            //playerData = new PlayerData(this);
            //sqlConfig = new SQLConfig(this);
        } catch (Exception e) {
            throw new StartupFailedException("Failed to load configuration files!", e);
        }

        try {
            translator = new PluginTranslator(this);
        } catch (Exception e) {
            throw new StartupFailedException("Failed to initialize the translator!", e);
        }

        try {
            //explosiveConfig = new ExplosiveConfig(this);
            //fireArmConfig = new FireArmConfig(this);
            //knifeConfig = new KnifeConfig(this);

            //getLogger().info("Succesfully loaded " + fireArmConfig.getWeaponList().size() + " guns, " +
            //        explosiveConfig.getWeaponList().size() + " explosives and "
            //        + knifeConfig.getWeaponList().size() + " knives from the config");
        } catch (Exception e) {
            throw new StartupFailedException("Failed to load weapon configuration files!", e);
        }

        //this.channelMessenger = new PluginChannelMessenger(this);
        this.gameManager = new BattleGameManager();

        //new DataLoader(this);

        //new WeaponsCommand(this);
        //new ZombiesCommand(this);

        //new BlockListener(this);
        //new ChatListener(this);
        //new EntityListener(this);
        //new GameListener(this);
        //new ItemListener(this);
        //new PlayerListener(this);

        //DAOFactory.init(this, sqlConfig);
        //WeaponsView.getInstance(this);
    }
}
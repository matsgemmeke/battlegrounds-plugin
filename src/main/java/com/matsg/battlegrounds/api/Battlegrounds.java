package com.matsg.battlegrounds.api;

import com.matsg.battlegrounds.api.config.BattlegroundsConfig;
import com.matsg.battlegrounds.api.config.CacheYaml;
import com.matsg.battlegrounds.api.config.WeaponConfig;
import com.matsg.battlegrounds.api.dao.PlayerDAOFactory;
import com.matsg.battlegrounds.api.item.Explosive;
import com.matsg.battlegrounds.api.item.FireArm;
import com.matsg.battlegrounds.api.item.Knife;
import org.bukkit.plugin.Plugin;

public interface Battlegrounds extends Plugin {

    /**
     * Gets the cache storage of the plugin
     *
     * @return The battlegrounds cache storage
     */
    CacheYaml getBattlegroundsCache();

    /**
     * Gets the configuration of the plugin.
     *
     * @return The battlegrounds configuation
     */
    BattlegroundsConfig getBattlegroundsConfig();

    /**
     * Gets the event manager of the plugin.
     *
     * @return The event manager
     */
    EventManager getEventManager();

    /**
     * Gets the explosives configrations of the plugin.
     *
     * @return The explosive configuration
     */
    WeaponConfig<Explosive> getExplosiveConfig();

    /**
     * Gets the firearms configrations of the plugin.
     *
     * @return The firearm configuration
     */
    WeaponConfig<FireArm> getFireArmConfig();

    /**
     * Gets the game manager of the plugin.
     *
     * @return The game manager
     */
    GameManager getGameManager();

    /**
     * Gets the knives configrations of the plugin.
     *
     * @return The knife configuration
     */
    WeaponConfig<Knife> getKnifeConfig();

    /**
     * Gets the player data storage of the plugin. This method will either return the factory
     * of the MySQL or local storage depending on whether the MySQL feature is enabled.
     *
     * @return The MySQL factory if enabled, local storage otherwise
     */
    PlayerDAOFactory getPlayerStorage();

    /**
     * Gets the translator of the plugin.
     *
     * @return The battlegrounds plugin translator
     */
    Translator getTranslator();

    /**
     * Reloads the configuration files. Note that this does not reload the games and arenas content.
     *
     * @return True if the new translations configuration was loaded successfully, false otherwise
     */
    boolean loadConfigs();
}
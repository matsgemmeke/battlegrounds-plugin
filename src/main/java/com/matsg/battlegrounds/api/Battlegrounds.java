package com.matsg.battlegrounds.api;

import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.storage.BattlegroundsConfig;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import com.matsg.battlegrounds.api.storage.LevelConfig;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.storage.PlayerStorage;
import org.bukkit.plugin.Plugin;

public interface Battlegrounds extends Plugin {

    /**
     * Gets the attachment factory of the plugin.
     *
     * @return the attachment factory
     */
    ItemFactory<Attachment> getAttachmentFactory();

    /**
     * Gets the cache storage of the plugin.
     *
     * @return the battlegrounds cache storage
     */
    CacheYaml getBattlegroundsCache();

    /**
     * Gets the configuration of the plugin.
     *
     * @return the battlegrounds configuation
     */
    BattlegroundsConfig getBattlegroundsConfig();

    /**
     * Gets the equipment factory of the plugin.
     *
     * @return the equipment factory
     */
    ItemFactory<Equipment> getEquipmentFactory();

    /**
     * Gets the event dispatcher of the plugin.
     *
     * @return the event dispatcher
     */
    EventDispatcher getEventDispatcher();

    /**
     * Gets the firearm factory of the plugin.
     *
     * @return the firearm factory
     */
    ItemFactory<Firearm> getFirearmFactory();

    /**
     * Gets the game manager of the plugin.
     *
     * @return the game manager
     */
    GameManager getGameManager();

    /**
     * Gets the level configurations of the plugin
     *
     * @return the level configuration
     */
    LevelConfig getLevelConfig();

    /**
     * Gets the melee weapon factory of the plugin.
     *
     * @return the melee weapon factory
     */
    ItemFactory<MeleeWeapon> getMeleeWeaponFactory();

    /**
     * Gets the player data storage of the plugin. This method will either return the factory
     * of the MySQL or local storage depending on whether the MySQL feature is enabled.
     *
     * @return the MySQL storage if enabled, local storage otherwise
     */
    PlayerStorage getPlayerStorage();

    /**
     * Gets the selection manager of the plugin.
     *
     * @return the selection manager.
     */
    SelectionManager getSelectionManager();

    /**
     * Gets the message translator of the plugin.
     *
     * @return the plugin message translator
     */
    Translator getTranslator();

    /**
     * Gets the NMS version of the plugin.
     *
     * @return the battlegrounds version for handling NMS related functions
     */
    Version getVersion();

    /**
     * Reloads the configuration files. Note that this does not reload the games and arenas content.
     *
     * @return true if the new translations configuration was loaded successfully, false otherwise
     */
    boolean loadConfigs();
}

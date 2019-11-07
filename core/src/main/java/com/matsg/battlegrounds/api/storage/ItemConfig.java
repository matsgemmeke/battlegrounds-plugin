package com.matsg.battlegrounds.api.storage;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public interface ItemConfig {

    /**
     * Gets the an item's configuration section. Returns null if it doesn't exist.
     *
     * @param id the id of the item
     * @return the corresponding item's configuration section
     */
    ConfigurationSection getItemConfigurationSection(String id);

    /**
     * Gets the list of configured items id's.
     *
     * @return a list containing all item id's
     */
    List<String> getItemList();

    /**
     * Gets the list of configured items id's by a certain item type.
     *
     * @param itemType the item type
     * @return a list containing all item id's by the specified item type
     */
    List<String> getItemList(String itemType);
}

package com.matsg.battlegrounds.api.config;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public interface ItemConfig {

    /**
     * Gets the an item's configuration section. Returns null if it doesn't exist.
     *
     * @param id The id of the item.
     * @return The corresponding item's configuration section.
     */
    ConfigurationSection getItemConfigurationSection(String id);

    /**
     * Gets the list of configured items id's.
     *
     * @return A list containing all item id's.
     */
    List<String> getItemList();

    /**
     * Gets the list of configured items id's by a certain item type.
     *
     * @param itemType The item type.
     * @return A list containing all item id's by the specified item type.
     */
    List<String> getItemList(String itemType);
}

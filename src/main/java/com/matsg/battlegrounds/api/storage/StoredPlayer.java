package com.matsg.battlegrounds.api.storage;

import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.entity.OfflineGamePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;

public interface StoredPlayer extends OfflineGamePlayer, Comparable<StoredPlayer> {

    /**
     * Adds and saves the statistic attributes of a player to their storage record.
     *
     * @param context The statistic context.
     * @return The updated player record, based on the statistic context.
     */
    StoredPlayer addStatisticAttributes(StatisticContext context);

    /**
     * Creates statistic attributes holding default values.
     *
     * @param player The Player instance of the player to be registered.
     */
    void createDefaultAttributes(Player player);

    /**
     * Gets a loadout setup based on the given number identifier.
     *
     * @param loadoutNr The loadout number identifier.
     * @return The corresponding loadout setup as a map.
     */
    Map<String, String> getLoadoutSetup(int loadoutNr);

    /**
     * Gets all loadout setups of the player.
     *
     * @return All loadout setups.
     */
    Collection<Map<String, String>> getLoadoutSetups();

    /**
     * Gets a statistic attribute.
     *
     * @param context The statistic context.
     * @return The statistic attribute, based on the statistic context or null if it doesn't exist.
     */
    int getStatisticAttribute(StatisticContext context);

    /**
     * Saves a loadout for the player.
     *
     * @param loadoutNumber The number identifier of the loadout
     * @param loadoutSetup The loadout setup to be saved.
     */
    void saveLoadout(int loadoutNumber, Map<String, String> loadoutSetup);

    /**
     * Updates the player's name in case of a name change.
     *
     * @param name The player's current name.
     */
    void updateName(String name);
}

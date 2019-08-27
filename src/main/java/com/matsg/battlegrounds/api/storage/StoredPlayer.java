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
     * @param context the statistic context
     * @return the updated player record, based on the statistic context
     */
    StoredPlayer addStatisticAttributes(StatisticContext context);

    /**
     * Creates statistic attributes holding default values.
     *
     * @param player the Player instance of the player to be registered
     */
    void createDefaultAttributes(Player player);

    /**
     * Gets a loadout setup based on the given number identifier.
     *
     * @param loadoutNr the loadout number identifier
     * @return the corresponding loadout setup as a map
     */
    Map<String, String> getLoadoutSetup(int loadoutNr);

    /**
     * Gets all loadout setups of the player.
     *
     * @return all loadout setups
     */
    Collection<Map<String, String>> getLoadoutSetups();

    /**
     * Gets a statistic attribute.
     *
     * @param context the statistic context
     * @return the statistic attribute, based on the statistic context or null if it doesn't exist
     */
    int getStatisticAttribute(StatisticContext context);

    /**
     * Saves a loadout for the player.
     *
     * @param loadoutNumber the number identifier of the loadout
     * @param loadoutSetup the loadout setup to be saved
     */
    void saveLoadout(int loadoutNumber, Map<String, String> loadoutSetup);

    /**
     * Updates the player's name in case of a name change.
     *
     * @param name the player's current name
     */
    void updateName(String name);
}

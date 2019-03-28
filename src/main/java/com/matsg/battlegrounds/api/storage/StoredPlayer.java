package com.matsg.battlegrounds.api.storage;

import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.entity.OfflineGamePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

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
     * @return The corresponding loadout.
     */
    Loadout getLoadout(int loadoutNr);

    /**
     * Gets a statistic attribute.
     *
     * @param context The statistic context.
     * @return The statistic attribute, based on the statistic context or null if it doesn't exist.
     */
    int getStatisticAttribute(StatisticContext context);

    /**
     * Gets all loadout setups of the player.
     *
     * @return All loadout setups.
     */
    Collection<Loadout> getLoadouts();

    /**
     * Saves a loadout for the player.
     *
     * @param loadoutNumber The number identifier of the loadout
     * @param loadout The loadout to be saved.
     */
    void saveLoadout(int loadoutNumber, Loadout loadout);

    /**
     * Updates the player's name in case of a name change.
     *
     * @param name The player's current name.
     */
    void updateName(String name);
}

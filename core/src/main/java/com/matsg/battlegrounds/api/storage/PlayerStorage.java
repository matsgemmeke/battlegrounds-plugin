package com.matsg.battlegrounds.api.storage;

import com.matsg.battlegrounds.api.entity.OfflineGamePlayer;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public interface PlayerStorage {

    /**
     * Checks whether the storage has a record of a uuid.
     *
     * @param uuid the uuid to check
     * @return whether the storage contains a record of this uuid
     */
    boolean contains(UUID uuid);

    /**
     * Gets a list of player by converting all records into objects.
     *
     * @return a list with all player records
     */
    List<? extends OfflineGamePlayer> getList();

    /**
     * Gets the player record of a certain uuid.
     *
     * @param uuid the uuid to get the player record of
     * @return the player object of this record or null if there is no record with the uuid
     */
    StoredPlayer getStoredPlayer(UUID uuid);

    /**
     * Gets a list of players with the best stats.
     *
     * @param limit the limit of amount of top players in the list
     * @return the top players list
     */
    List<? extends OfflineGamePlayer> getTopPlayers(int limit);

    /**
     * Creates a new player record for a player.
     *
     * @param player the player to be registered.
     * @return the new player record converted to a stored player object.
     */
    StoredPlayer registerPlayer(OfflinePlayer player);

    /**
     * Updates a player's record attributes.
     *
     * @param player the player to update its record for.
     * @return the updated player record.
     */
    StoredPlayer updatePlayer(OfflinePlayer player);
}

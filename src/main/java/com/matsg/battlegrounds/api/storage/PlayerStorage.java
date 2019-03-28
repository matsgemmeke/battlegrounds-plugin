package com.matsg.battlegrounds.api.storage;

import com.matsg.battlegrounds.api.entity.OfflineGamePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface PlayerStorage {

    /**
     * Checks whether the storage has a record of a uuid.
     *
     * @param uuid The uuid to check
     * @return Whether the storage contains a record of this uuid
     */
    boolean contains(UUID uuid);

    /**
     * Gets a list of player by converting all records into objects.
     *
     * @return A list with all player records
     */
    List<? extends OfflineGamePlayer> getList();

    /**
     * Gets the player record of a certain uuid.
     *
     * @param uuid The uuid to get the player record of
     * @return The player object of this record or null if there is no record with the uuid
     */
    StoredPlayer getStoredPlayer(UUID uuid);

    /**
     * Gets a list of players with the best stats.
     *
     * @param limit The limit of amount of top players in the list
     * @return The top players list
     */
    List<? extends OfflineGamePlayer> getTopPlayers(int limit);

    /**
     * Creates a new player record for a player.
     *
     * @param player The player to be registered.
     * @return The new player record converted to a stored player object.
     */
    StoredPlayer registerPlayer(Player player);

    /**
     * Updates a player's record attributes.
     *
     * @param player The player to update its record for.
     * @return The updated player record.
     */
    StoredPlayer updatePlayer(Player player);
}

package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface PlayerManager {

    /**
     * Adds a player to a game.
     *
     * @param player The player to add to the game
     * @return The created GamePlayer instance of the player
     */
    GamePlayer addPlayer(Player player);

    /**
     * Sends a message to all players in the game.
     *
     * @param message The message to send
     */
    void broadcastMessage(String message);

    /**
     * Changes the loadout of a player.
     *
     * @param gamePlayer The player to change the loadout of
     * @param loadout The new loadout for the player
     * @param apply Whether the player receives the items right away or when they respawn
     */
    void changeLoadout(GamePlayer gamePlayer, Loadout loadout, boolean apply);

    /**
     * Resets a player to their default state without removing them from the game.
     *
     * @param gamePlayer The player to clear
     */
    void clearPlayer(GamePlayer gamePlayer);

    /**
     * Damages a player.
     *
     * @param gamePlayer The player to damage
     * @param damage The damage amount
     */
    void damagePlayer(GamePlayer gamePlayer, double damage);

    /**
     * Damages a player.
     *
     * @param gamePlayer The player to damage
     * @param damage The damage amount
     * @param effect Display a damage effect or not
     */
    void damagePlayer(GamePlayer gamePlayer, double damage, boolean effect);

    /**
     * Gets the GamePlayer instance of a player
     *
     * @param player The player to get the GamePlayer instance of
     * @return The GamePlayer instance or null if the player is not in the game
     */
    GamePlayer getGamePlayer(Player player);

    /**
     * Gets the currently living players.
     *
     * @return An array containing all active players
     */
    GamePlayer[] getLivingPlayers();

    /**
     * Gets the currently living players of a team.
     *
     * @param team The team to get the players from
     * @return An array containing all active players in this team
     */
    GamePlayer[] getLivingPlayers(Team team);

    /**
     * Gets nearby enemy players of a player.
     *
     * @param team The team to find enemy players of
     * @param location The location to look for enemy players
     * @param range The maximum distance between enemy and the player
     * @return An array of nearby enemy players
     */
    GamePlayer[] getNearbyEnemyPlayers(Team team, Location location, double range);

    /**
     * Gets nearby players from a location.
     *
     * @param location The location to get nearby players of
     * @param range The maximum distance between player and the location
     * @return An array of nearby players
     */
    GamePlayer[] getNearbyPlayers(Location location, double range);

    /**
     * Gets the nearest player from a location.
     *
     * @param location The location to get the nearest player of
     * @return The nearest player
     */
    GamePlayer getNearestPlayer(Location location);

    /**
     * Gets the nearest player from a location within a certain range.
     *
     * @param location The location to get the nearest player of
     * @param range The maximum distance between player and the location
     * @return The nearest player or null if there are no players in the range
     */
    GamePlayer getNearestPlayer(Location location, double range);

    /**
     * Gets the nearest player from a certain from a location.
     *
     * @param location The location to get the nearest player of
     * @param team The team the player has to be in
     * @return The nearest player of this team or null if there are no players in this team
     */
    GamePlayer getNearestPlayer(Location location, Team team);

    /**
     * Gets the nearest player from a certain from a location within a certain range.
     *
     * @param location The location to get the nearest player of
     * @param team The team the player has to be in
     * @param range The maximum distance between player and the location
     * @return The nearest player of this team or null if there are no players in this team or there are no players from this team in the range
     */
    GamePlayer getNearestPlayer(Location location, Team team, double range);

    /**
     * Gets all players.
     *
     * @return All players in the game
     */
    Collection<GamePlayer> getPlayers();

    /**
     * Equips the player with the default items.
     *
     * @param gamePlayer The player to prepare
     */
    void preparePlayer(GamePlayer gamePlayer);

    /**
     * Removes a player from the game.
     *
     * @param gamePlayer The player to remove
     * @return Whether the player was removed from the game
     */
    boolean removePlayer(GamePlayer gamePlayer);

    /**
     * Handles a player respawn.
     *
     * @param gamePlayer The player to respawn
     * @param spawn The spawn location
     */
    void respawnPlayer(GamePlayer gamePlayer, Spawn spawn);

    /**
     * Sets a player visible to other players.
     *
     * @param gamePlayer The player to make visible or not
     * @param visible Visible or not
     */
    void setVisible(GamePlayer gamePlayer, boolean visible);

    /**
     * Sets a player visible to other players in a certain team.
     *
     * @param gamePlayer The player to make visible or not
     * @param team The team to perform the action on
     * @param visible Visible or not
     */
    void setVisible(GamePlayer gamePlayer, Team team, boolean visible);

    /**
     * Updates a player's exp bar according to their game exp.
     *
     * @param gamePlayer The player to update the exp bar of
     */
    void updateExpBar(GamePlayer gamePlayer);
}

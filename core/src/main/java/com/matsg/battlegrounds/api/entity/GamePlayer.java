package com.matsg.battlegrounds.api.entity;

import com.matsg.battlegrounds.api.game.SpawnOccupant;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.Loadout;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface GamePlayer extends BattleEntity, OfflineGamePlayer, SpawnOccupant, Comparable<GamePlayer> {

    /**
     * Gets the down state of the player. Returns null if the player is not downed.
     *
     * @return the player down state
     */
    @Nullable
    DownState getDownState();

    /**
     * Sets the down state of the player.
     *
     * @param downState the player down state
     */
    void setDownState(@Nullable DownState downState);

    /**
     * Gets the effects casted on the player.
     *
     * @return the player's effects
     */
    Collection<PlayerEffect> getEffects();

    /**
     * Gets the items the player is holding.
     *
     * @return the player's items
     */
    Collection<Item> getItems();

    /**
     * Gets the amount of lives the player has.
     *
     * @return the amount of lives
     */
    int getLives();

    /**
     * Sets the amount of lives the player has.
     *
     * @param lives the amount of lives
     */
    void setLives(int lives);

    /**
     * Gets the loadout the player is currently using.
     *
     * @return the player's current loadout
     */
    Loadout getLoadout();

    /**
     * Sets the loadout the player is currently using.
     *
     * @param loadout the player's current loadout
     */
    void setLoadout(Loadout loadout);

    /**
     * Gets the player entity of the player.
     *
     * @return the player entity
     */
    Player getPlayer();

    /**
     * Gets the amount of points the player has.
     *
     * @return the player's points
     */
    int getPoints();

    /**
     * Sets the amount of points the player has/
     *
     * @param points the player's points
     */
    void setPoints(int points);

    /**
     * Gets the return location of the player for when their game has ended.
     *
     * @return the player's return location
     */
    Location getReturnLocation();

    /**
     * Sets the return location of the player for when their game has ended.
     *
     * @param returnLocation the player's return location
     */
    void setReturnLocation(Location returnLocation);

    /**
     * Gets the amount of time the player needs to revive another player.
     *
     * @return the revive duration in ticks
     */
    float getReviveDuration();

    /**
     * Sets the amount of time the player needs to revive another player.
     *
     * @param reviveDuration the revive duration in ticks
     */
    void setReviveDuration(float reviveDuration);

    /**
     * Gets the inventory the player held prior to joining a game.
     *
     * @return the player's saved inventory
     */
    SavedInventory getSavedInventory();

    /**
     * Gets the loadout the player has selected. This is not necessarily the loadout
     * the player is currently using.
     *
     * @return the player's selected loadout
     */
    Loadout getSelectedLoadout();

    /**
     * Sets the loadout the player has selected.
     *
     * @param loadout the player's selected loadout
     */
    void setSelectedLoadout(Loadout loadout);

    /**
     * Gets the player's current state
     *
     * @return the player state
     */
    PlayerState getState();

    /**
     * Gets the team the player is on.
     *
     * @return the player's team
     */
    Team getTeam();

    /**
     * Sets the team the player is on.
     *
     * @param team the player's team
     */
    void setTeam(Team team);

    /**
     * Gives the player a certain amount of exp.
     *
     * @param exp the amount of exp
     * @return the amount of exp the player has after the given exp was added
     */
    int addExp(int exp);

    /**
     * Changes the player's current state and applies it to the player.
     *
     * @param playerState the player state
     */
    void changeState(PlayerState playerState);

    /**
     * Gets a weapon from a certain slot in the player's inventory. Returns null if
     * the given inventory slot does not contain a weapon.
     *
     * @param slot the inventory slot
     * @return the weapon which is in the given inventory slot
     */
    // Weapon getWeaponFromSlot(int slot);

    /**
     * Refreshes the player's current effect.
     */
    void refreshEffects();

    /**
     * Sends a message to the player.
     *
     * @param message the message to be sent
     */
    void sendMessage(String message);
}

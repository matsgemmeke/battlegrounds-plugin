package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;

public interface Team extends SpawnOccupant {

    /**
     * Gets the armor color of the team
     *
     * @return the team's armor color
     */
    Color getArmorColor();

    /**
     * Gets the chat color of the team.
     *
     * @return the team's chat color
     */
    ChatColor getChatColor();

    /**
     * Gets the id of the team
     *
     * @return the team's id
     */
    int getId();

    /**
     * Gets the kill count of the team.
     *
     * @return the team's kill count
     */
    int getKills();

    /**
     * Gets the name of the team.
     *
     * @return the team's name
     */
    String getName();

    /**
     * Gets the player in the team.
     *
     * @return an array containg the team's players
     */
    GamePlayer[] getPlayers();

    /**
     * Gets the total score of the team.
     *
     * @return the team's total score
     */
    int getScore();

    /**
     * Sets the total score of the team.
     *
     * @param score the team's total score
     */
    void setScore(int score);

    /**
     * Gets the amount of players in the team.
     *
     * @return the team player count
     */
    int getTeamSize();

    /**
     * Adds a player to the team.
     *
     * @param gamePlayer the player to add
     */
    void addPlayer(GamePlayer gamePlayer);

    /**
     * Gets whether the team has a certain player in it.
     *
     * @param gamePlayer the player
     * @return whether the team already has the player
     */
    boolean hasPlayer(GamePlayer gamePlayer);

    /**
     * Removes a player from the team.
     *
     * @param gamePlayer the player to remove
     */
    void removePlayer(GamePlayer gamePlayer);
}

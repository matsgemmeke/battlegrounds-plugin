package com.matsg.battlegrounds.api.storage;

import com.matsg.battlegrounds.api.entity.OfflineGamePlayer;
import com.matsg.battlegrounds.api.game.Game;

public class StatisticContext {

    private Game game;
    private OfflineGamePlayer player;
    private String statisticName;

    /**
     * Gets the game of which the statistic applies to.
     *
     * @return The game of the statistic.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Sets the game of which the statistic applies to.
     *
     * @param game The game of the statistic.
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Gets the player of which the statistic applies to.
     *
     * @return The player of the statistic.
     */
    public OfflineGamePlayer getPlayer() {
        return player;
    }

    /**
     * Sets the player of which the statistic applies to.
     *
     * @param player The player of the statistic.
     */
    public void setPlayer(OfflineGamePlayer player) {
        this.player = player;
    }

    /**
     * Gets the statistic attribute name.
     *
     * @return The statistic attribute name.
     */
    public String getStatisticName() {
        return statisticName;
    }

    /**
     * Sets the statistic attribute name.
     *
     * @param statisticName The statistic attribute name.
     */
    public void setStatisticName(String statisticName) {
        this.statisticName = statisticName;
    }
}

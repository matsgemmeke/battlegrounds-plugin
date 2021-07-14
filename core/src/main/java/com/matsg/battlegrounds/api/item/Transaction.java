package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;

public class Transaction {

    private Game game;
    private GamePlayer gamePlayer;
    private int points;
    private int slot;
    private TransactionItem item;

    public Transaction(Game game, GamePlayer gamePlayer, TransactionItem item, int points, int slot) {
        this.game = game;
        this.gamePlayer = gamePlayer;
        this.item = item;
        this.points = points;
        this.slot = slot;
    }

    /**
     * Gets the game in which the transaction took place.
     *
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Gets the player who makes the transaction.
     *
     * @return the player of the transaction
     */
    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    /**
     * Gets the item of the transaction.
     *
     * @return the transaction item
     */
    public TransactionItem getItem() {
        return item;
    }

    /**
     * Gets the amount of points involved in the transaction.
     *
     * @return the amount of points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Gets the inventory slot to which the transaction item will be transferred to.
     *
     * @return the transaction inventory slot
     */
    public int getSlot() {
        return slot;
    }
}

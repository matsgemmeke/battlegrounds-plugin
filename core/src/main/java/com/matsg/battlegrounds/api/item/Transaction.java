package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;

public class Transaction {

    private Game game;
    private GamePlayer gamePlayer;
    private int points;
    private int slot;
    private TransactionItem item;

    /**
     * Gets the game in which the transaction took place.
     *
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Sets the game in which the transaction took place.
     *
     * @param game the game
     */
    public void setGame(Game game) {
        this.game = game;
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
     * Sets the player who makes the transaction.
     *
     * @param gamePlayer the player of the transaction
     */
    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
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
     * Sets the item of the transaction.
     *
     * @param item the transaction item
     */
    public void setItem(TransactionItem item) {
        this.item = item;
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
     * Sets the amount of points involved in the transaction.
     *
     * @param points the amount of points
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Gets the inventory slot to which the transaction item will be transferred to.
     *
     * @return the transaction inventory slot
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Sets the inventory slot to which the transaction item will be transferred to.
     *
     * @param slot the transaction inventory slot
     */
    public void setSlot(int slot) {
        this.slot = slot;
    }
}

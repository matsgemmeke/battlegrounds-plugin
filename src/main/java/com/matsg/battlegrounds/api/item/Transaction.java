package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.entity.GamePlayer;

public class Transaction {

    private GamePlayer gamePlayer;
    private int points;
    private ItemSlot itemSlot;
    private TransactionItem item;

    /**
     * Gets the player who makes the transaction.
     *
     * @return The player of the transaction.
     */
    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    /**
     * Sets the player who makes the transaction.
     *
     * @param gamePlayer The player of the transaction.
     */
    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    /**
     * Gets the item of the transaction.
     *
     * @return The transaction item.
     */
    public TransactionItem getItem() {
        return item;
    }

    /**
     * Sets the item of the transaction.
     *
     * @param item The transaction item.
     */
    public void setItem(TransactionItem item) {
        this.item = item;
    }

    /**
     * Gets the item slot to which the transaction item will be transferred to.
     *
     * @return The transaction item slot.
     */
    public ItemSlot getItemSlot() {
        return itemSlot;
    }

    /**
     * Sets the item slot to which the transaction item will be transferred to.
     *
     * @param itemSlot The transaction item slot.
     */
    public void setItemSlot(ItemSlot itemSlot) {
        this.itemSlot = itemSlot;
    }

    /**
     * Gets the amount of points involved in the transaction.
     *
     * @return The amount of points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets the amount of points involved in the transaction.
     *
     * @param points The amount of points.
     */
    public void setPoints(int points) {
        this.points = points;
    }
}

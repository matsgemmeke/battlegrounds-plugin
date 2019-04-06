package com.matsg.battlegrounds.api.item;

public interface TransactionItem {

    /**
     * Handles a transaction for the purchase of this item.
     *
     * @param transaction The transaction instance.
     */
    void handleTransaction(Transaction transaction);
}

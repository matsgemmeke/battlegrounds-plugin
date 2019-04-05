package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.entity.GamePlayer;

public interface TransactionItem {

    /**
     * Handles a transaction for the purchase of this item.
     *
     * @param gamePlayer The player to purchase the item.
     * @param itemSlot The item slot to which the item will be transferred to.
     * @return A transaction object containing involved instances.
     */
    Transaction handleTransaction(GamePlayer gamePlayer, ItemSlot itemSlot);
}

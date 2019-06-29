package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.game.WeaponUsageContext;

public interface Weapon extends Item, PlayerProperty, TransactionItem {

    WeaponUsageContext getContext();

    void setContext(WeaponUsageContext context);

    String getDescription();

    ItemSlot getItemSlot();

    void setItemSlot(ItemSlot itemSlot);

    ItemType getType();

    Weapon clone();

    void resetState();
}

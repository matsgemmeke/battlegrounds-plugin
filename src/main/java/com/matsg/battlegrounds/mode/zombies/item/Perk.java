package com.matsg.battlegrounds.mode.zombies.item;

import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.PlayerProperty;
import com.matsg.battlegrounds.api.item.TransactionItem;

public interface Perk extends Item, PlayerProperty, TransactionItem {

    PerkEffect getEffect();

    Perk clone();
}

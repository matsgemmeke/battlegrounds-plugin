package com.matsg.battlegrounds.api.item;

public interface Perk extends Item, PlayerProperty, TransactionItem {

    PerkEffect getEffect();

    Perk clone();
}

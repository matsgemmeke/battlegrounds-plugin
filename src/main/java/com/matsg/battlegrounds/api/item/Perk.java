package com.matsg.battlegrounds.api.item;

public interface Perk extends Item, Property, TransactionItem {

    Perk clone();

    PerkEffect getEffect();
}

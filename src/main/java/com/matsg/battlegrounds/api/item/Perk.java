package com.matsg.battlegrounds.api.item;

public interface Perk extends Item, Property, TransactionItem {

    PerkEffect getEffect();

    Perk clone();
}

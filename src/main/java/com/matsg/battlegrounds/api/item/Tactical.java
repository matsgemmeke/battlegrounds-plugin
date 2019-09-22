package com.matsg.battlegrounds.api.item;

public interface Tactical extends Equipment {

    TacticalEffect getEffect();

    void setEffect(TacticalEffect effect);

    Tactical clone();
}

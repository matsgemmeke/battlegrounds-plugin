package com.matsg.battlegrounds.api.item;

public interface Tactical extends Explosive {

    Tactical clone();

    enum TacticalEffect {

        BAIT, DISORIENTATION, SLOWNESS;
    }
}

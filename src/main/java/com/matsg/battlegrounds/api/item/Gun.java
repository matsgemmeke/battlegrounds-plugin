package com.matsg.battlegrounds.api.item;

public interface Gun extends FireArm {

    int getBurstRounds();

    int getFireRate();

    WeaponType getType();
}
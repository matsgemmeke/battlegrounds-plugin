package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.util.Sound;

public interface Explosive extends Weapon, Explodable, Projectile {

    Explosive clone();

    int getAmount();

    Iterable<Item> getDroppedItems();

    Sound[] getExplodeSound();

    int getIgnitionTime();

    int getMaxAmount();

    double getVelocity();

    boolean isBeingThrown();

    void setAmount(int amount);

    void setVelocity(double velocity);

    void throwExplosive();

    void throwExplosive(double velocity);
}
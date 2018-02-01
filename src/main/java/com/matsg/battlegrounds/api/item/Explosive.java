package com.matsg.battlegrounds.api.item;

public interface Explosive extends Weapon {

    Explosive clone();

    int getAmount();

    int getMaxAmount();

    double getRange();

    double getVelocity();

    void setAmount(int amount);

    void setRange(double range);

    void setVelocity(double velocity);

    void throwExplosive();
}
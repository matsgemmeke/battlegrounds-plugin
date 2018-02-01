package com.matsg.battlegrounds.api.item;

public interface Lethal extends Explosive {

    Lethal clone();

    double getDamage();

    void setDamage(double damage);
}

package com.matsg.battlegrounds.api.item;

public interface Knife extends Weapon {

    Knife clone();

    int getAmount();

    double getDamage();

    int getMaxAmount();

    boolean isThrowable();

    void setAmount(int amount);

    void setDamage(double damage);

    void shoot();
}
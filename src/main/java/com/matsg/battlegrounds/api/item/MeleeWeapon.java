package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.entity.GamePlayer;

public interface MeleeWeapon extends Weapon, Droppable {

    int getAmount();

    void setAmount(int amount);

    double getDamage();

    void setDamage(double damage);

    int getMaxAmount();

    boolean isThrowable();

    MeleeWeapon clone();

    double damage(GamePlayer gamePlayer);

    void shoot();
}

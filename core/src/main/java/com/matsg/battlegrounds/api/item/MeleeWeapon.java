package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.entity.BattleEntity;

public interface MeleeWeapon extends Weapon {

    int getAmount();

    void setAmount(int amount);

    double getDamage();

    void setDamage(double damage);

    int getMaxAmount();

    boolean isThrowable();

    MeleeWeapon clone();

    double damage(BattleEntity entity);

    void shoot();
}

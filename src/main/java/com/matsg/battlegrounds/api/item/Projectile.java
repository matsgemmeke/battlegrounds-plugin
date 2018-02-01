package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.util.Hitbox;

public interface Projectile {

    double getAccuracy();

    double getDamage(Hitbox hitbox, double distance);

    double getHeadshotMultiplier();

    double getLongDamage();

    double getLongRange();

    double getMidDamage();

    double getMidRange();

    double getShortDamage();

    double getShortRange();

    void setAccuracy(double accuracy);

    void setHeadshotMultiplier(double headshotMultiplier);

    void setLongDamage(int longDamage);

    void setLongRange(int longRange);

    void setMidDamage(int midDamage);

    void setMidRange(int midRange);

    void setPierce(boolean pierce);

    void setShortDamage(int shortDamage);

    void setShortRange(int shortRange);
}
package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.util.Hitbox;

public interface Projectile {

    double getDamage(Hitbox hitbox, double distance);

    double getLongDamage();

    double getLongRange();

    double getMidDamage();

    double getMidRange();

    double getShortDamage();

    double getShortRange();

    void setLongDamage(double longDamage);

    void setLongRange(double longRange);

    void setMidDamage(double midDamage);

    void setMidRange(double midRange);

    void setShortDamage(double shortDamage);

    void setShortRange(double shortRange);
}
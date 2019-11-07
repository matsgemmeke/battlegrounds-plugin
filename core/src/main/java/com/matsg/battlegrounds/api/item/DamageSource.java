package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.entity.Hitbox;

public interface DamageSource {

    double getLongDamage();

    void setLongDamage(double longDamage);

    double getLongRange();

    void setLongRange(double longRange);

    double getMidDamage();

    void setMidDamage(double midDamage);

    double getMidRange();

    void setMidRange(double midRange);

    double getShortDamage();

    void setShortDamage(double shortDamage);

    double getShortRange();

    void setShortRange(double shortRange);

    double getDamage(Hitbox hitbox, double distance);
}

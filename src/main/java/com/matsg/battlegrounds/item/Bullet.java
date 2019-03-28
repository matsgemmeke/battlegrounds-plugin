package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.DamageSource;
import com.matsg.battlegrounds.api.entity.Hitbox;

public class Bullet implements DamageSource {

    private double headshotMultiplier, longDamage, longRange, midDamage, midRange, shortDamage, shortRange;

    public Bullet(double longDamage, double longRange, double midDamage, double midRange,
                             double shortDamage, double shortRange, double headshotMultiplier) {
        this.headshotMultiplier = headshotMultiplier;
        this.longDamage = longDamage;
        this.longRange = longRange;
        this.midDamage = midDamage;
        this.midRange = midRange;
        this.shortDamage = shortDamage;
        this.shortRange = shortRange;
    }

    public double getLongDamage() {
        return longDamage;
    }

    public double getLongRange() {
        return longRange;
    }

    public double getMidDamage() {
        return midDamage;
    }

    public double getMidRange() {
        return midRange;
    }

    public double getShortDamage() {
        return shortDamage;
    }

    public double getShortRange() {
        return shortRange;
    }

    public void setLongDamage(double longDamage) {
        this.longDamage = longDamage;
    }

    public void setLongRange(double longRange) {
        this.longRange = longRange;
    }

    public void setMidDamage(double midDamage) {
        this.midDamage = midDamage;
    }

    public void setMidRange(double midRange) {
        this.midRange = midRange;
    }

    public void setShortDamage(double shortDamage) {
        this.shortDamage = shortDamage;
    }

    public void setShortRange(double shortRange) {
        this.shortRange = shortRange;
    }

    public double getDamage(Hitbox hitbox, double distance) {
        if (hitbox == null) {
            return 0.0;
        }
        return getDistanceDamage(distance) * (hitbox == Hitbox.HEAD ? headshotMultiplier : hitbox.getDamageMultiplier());
    }

    private double getDistanceDamage(double distance) {
        if (distance <= shortRange) {
            return shortDamage;
        } else if (distance > shortRange && distance <= midRange) {
            return midDamage;
        } else if (distance > midRange && distance <= longRange) {
            return longDamage;
        }
        return 0.0;
    }
}

package com.matsg.battlegrounds.api.util;

public enum Hitbox {

    HEAD(2.0, 1.4, 1.0, 100),
    LEG(0.8, 0.0, 0.5, 50),
    TORSO(1.4, 0.8, 1.0, 60);

    private double damageMultiplier, maxHeight, minHeight;
    private int points;

    Hitbox(double maxHeight, double minHeight, double damageMultiplier, int points) {
        this.damageMultiplier = damageMultiplier;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.points = points;
    }

    public static Hitbox getHitbox(double entityHeight, double projectileHeight) {
        double dif = projectileHeight - entityHeight;
        for (Hitbox hitbox : Hitbox.values()) {
            if (dif >= hitbox.getMinHeight() && dif < hitbox.getMaxHeight()) {
                return hitbox;
            }
        }
        return null;
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public double getMinHeight() {
        return minHeight;
    }

    public int getPoints() {
        return points;
    }
}
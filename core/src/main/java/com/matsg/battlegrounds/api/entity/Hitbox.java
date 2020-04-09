package com.matsg.battlegrounds.api.entity;

public enum Hitbox {

    HEAD("game-death-headshot", 1.8, 1.4, 1.0, 100),
    LEG("game-death-player-kill", 0.8, 0.0, 0.5, 50),
    TORSO("game-death-player-kill", 1.4, 0.8, 1.0, 60);

    public static final double MAX_ENTITY_HEIGHT = 1.8;

    private double damageMultiplier, maxHeight, minHeight;
    private int points;
    private String killMessageKey;

    Hitbox(String killMessageKey, double maxHeight, double minHeight, double damageMultiplier, int points) {
        this.killMessageKey = killMessageKey;
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

        throw new IllegalArgumentException("No hitbox available for entity height " + entityHeight + " and projectile height " + projectileHeight);
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public String getKillMessageKey() {
        return killMessageKey;
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

package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.api.entity.Zombie;
import com.matsg.battlegrounds.api.game.MobSpawn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ZombiesWave implements Wave<Zombie> {

    private boolean running;
    private double attackDamage;
    private double followRange;
    private double maxHealth;
    private int mobCount;
    private int round;
    private List<MobSpawn> mobSpawns;
    private List<Zombie> zombies;
    private Random random;

    public ZombiesWave(int round, int mobCount, double attackDamage, double followRange, double maxHealth) {
        this.round = round;
        this.mobCount = mobCount;
        this.attackDamage = attackDamage;
        this.followRange = followRange;
        this.maxHealth = maxHealth;
        this.mobSpawns = new ArrayList<>();
        this.random = new Random();
        this.zombies = new ArrayList<>();
        this.running = false;
    }

    public int getMobCount() {
        return mobCount;
    }

    public List<Zombie> getMobs() {
        return zombies;
    }

    public List<MobSpawn> getMobSpawns() {
        return mobSpawns;
    }

    public int getRound() {
        return round;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Zombie nextMob() {
        double movementSpeed = 0.2;
        float health = getHealth();

        if (random.nextDouble() <= (round * 0.1 - 0.1)) {
            movementSpeed += ((random.nextDouble() / 10) * 1.1 + 0.05);
        }

        Zombie zombie = zombies.get(0);
        zombie.setAttackDamage(attackDamage);
        zombie.setFollowRange(followRange);
        zombie.setHealth(health);
        zombie.setKnockback(false);
        zombie.setMaxHealth(health);
        zombie.setMovementSpeed(movementSpeed);

        return zombies.get(0);
    }

    private float getHealth() {
        if (round < 10) {
            return (float) (5.0 + 10.0 * round);
        }

        double health = Math.round(Math.pow(1.2, round) + (15.0 * round));

        if (health > maxHealth) {
            health = maxHealth; // Set the configured max health value
        }

        return (float) health;
    }
}

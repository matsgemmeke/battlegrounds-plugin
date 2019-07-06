package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.api.entity.Hellhound;
import com.matsg.battlegrounds.mode.zombies.component.MobSpawn;

import java.util.ArrayList;
import java.util.List;

public class HellhoundWave implements Wave<Hellhound> {

    private boolean running;
    private double attackDamage;
    private double followRange;
    private float health;
    private int mobCount;
    private int round;
    private List<Hellhound> hellhounds;
    private List<MobSpawn> mobSpawns;

    public HellhoundWave(int round, int mobCount, double attackDamage, double followRange, float health) {
        this.round = round;
        this.mobCount = mobCount;
        this.attackDamage = attackDamage;
        this.followRange = followRange;
        this.health = health;
        this.hellhounds = new ArrayList<>();
        this.mobSpawns = new ArrayList<>();
        this.running = false;
    }

    public int getMobCount() {
        return mobCount;
    }

    public List<Hellhound> getMobs() {
        return hellhounds;
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

    public Hellhound nextMob() {
        return hellhounds.get(0);
    }
}

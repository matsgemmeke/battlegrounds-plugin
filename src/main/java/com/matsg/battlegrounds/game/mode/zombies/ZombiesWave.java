package com.matsg.battlegrounds.game.mode.zombies;

import com.matsg.battlegrounds.api.entity.Zombie;
import com.matsg.battlegrounds.api.game.MobSpawn;

import java.util.ArrayList;
import java.util.List;

public class ZombiesWave implements Wave<Zombie> {

    private boolean running;
    private int round;
    private List<MobSpawn> mobSpawns;
    private List<Zombie> zombies;

    public ZombiesWave(int round) {
        this.round = round;
        this.mobSpawns = new ArrayList<>();
        this.zombies = new ArrayList<>();
        this.running = false;
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
        return zombies.get(0);
    }
}

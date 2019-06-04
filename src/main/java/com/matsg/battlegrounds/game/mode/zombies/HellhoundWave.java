package com.matsg.battlegrounds.game.mode.zombies;

import com.matsg.battlegrounds.api.entity.Hellhound;
import com.matsg.battlegrounds.api.game.MobSpawn;

import java.util.ArrayList;
import java.util.List;

public class HellhoundWave implements Wave<Hellhound> {

    private boolean running;
    private int round;
    private List<Hellhound> hellhounds;
    private List<MobSpawn> mobSpawns;

    public HellhoundWave(int round) {
        this.round = round;
        this.hellhounds = new ArrayList<>();
        this.mobSpawns = new ArrayList<>();
        this.running = false;
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

    public void startWave() {

    }
}

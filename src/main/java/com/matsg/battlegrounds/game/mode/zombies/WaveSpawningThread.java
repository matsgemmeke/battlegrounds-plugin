package com.matsg.battlegrounds.game.mode.zombies;

import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.game.MobSpawn;
import com.matsg.battlegrounds.util.BattleRunnable;

import java.util.List;
import java.util.Random;

public class WaveSpawningThread extends BattleRunnable {

    private int maxMobs;
    private Random random;
    private Wave wave;
    private Zombies zombies;

    public WaveSpawningThread(Zombies zombies, Wave wave, int maxMobs) {
        this.zombies = zombies;
        this.wave = wave;
        this.maxMobs = maxMobs;
        this.random = new Random();
    }

    public void run() {
        if (!wave.isRunning()) {
            cancel();
            return;
        }

        // In case there are too many mobs, this skips the task and repeats it later when more mobs have been killed.
        if (zombies.getMobManager().getMobs().size() > maxMobs) {
            return;
        }

        List<MobSpawn> spawns = wave.getMobSpawns();

        if (spawns.size() <= 0) {
            cancel();
            return;
        }

        Mob mob = wave.nextMob();
        MobSpawn mobSpawn;
        int tries = 0;

        do {
            mobSpawn = spawns.get(random.nextInt(spawns.size()));
            if (++ tries > 100)
                return; // Prevention of infinite loops
        } while (mobSpawn.isLocked() || !mobSpawn.getSpawnLocation(mob.getMobType()).getChunk().isLoaded());

        wave.getMobs().remove(mob);
        zombies.getMobManager().getMobs().add(mob);

        mob.spawn(mobSpawn);

        if (zombies.getMobManager().getMobs().size() >= maxMobs) {
            wave.setRunning(false);
            cancel();
        }
    }
}

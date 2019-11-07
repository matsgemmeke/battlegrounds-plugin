package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.MobSpawn;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class WaveSpawningThread extends BukkitRunnable {

    private Game game;
    private int maxMobs;
    private int spawnedCount;
    private Random random;
    private Wave wave;

    public WaveSpawningThread(Game game, Wave wave, int maxMobs) {
        this.game = game;
        this.wave = wave;
        this.maxMobs = maxMobs;
        this.random = new Random();
        this.spawnedCount = 0;
    }

    public void run() {
        if (!wave.isRunning()) {
            cancel();
            return;
        }

        // In case there are too many mobs, this skips the task and repeats it later when more mobs have been killed.
        if (game.getMobManager().getMobs().size() > maxMobs) {
            return;
        }

        List<MobSpawn> spawns = wave.getMobSpawns();

        if (spawns.size() <= 0 || wave.getMobs().size() <= 0) {
            cancel();
            return;
        }

        Mob mob = wave.nextMob();
        MobSpawn mobSpawn;
        int tries = 0;

        do {
            mobSpawn = spawns.get(random.nextInt(spawns.size()));
            if (++tries > 100)
                return; // Prevention of infinite loops
        } while (mobSpawn.isLocked() || !mobSpawn.getSpawnLocation(mob.getEntityType()).getChunk().isLoaded());

        wave.getMobs().remove(mob);
        game.getMobManager().getMobs().add(mob);
        game.updateScoreboard();

        mob.spawn(mobSpawn.getSpawnLocation(mob.getEntityType()), mobSpawn.getBarricade());
        mob.getBukkitEntity().setCustomName(game.getMobManager().getHealthBar(mob));

        if (++spawnedCount >= wave.getMobCount()) {
            wave.setRunning(false);
            cancel();
        }
    }
}

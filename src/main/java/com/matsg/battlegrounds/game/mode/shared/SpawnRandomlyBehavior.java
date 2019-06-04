package com.matsg.battlegrounds.game.mode.shared;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Spawn;

public class SpawnRandomlyBehavior implements SpawningBehavior {

    private Arena arena;

    public SpawnRandomlyBehavior(Arena arena) {
        this.arena = arena;
    }

    public SpawningResult spawnPlayers(Iterable<GamePlayer> players) {
        if (arena.getSpawnCount() <= 0) {
            return new SpawningResult("No spawns were found in arena " + arena.getName());
        }

        for (GamePlayer gamePlayer : players) {
            Spawn spawn = arena.getRandomSpawn();
            spawn.setGamePlayer(gamePlayer);

            gamePlayer.getPlayer().teleport(spawn.getLocation());
        }

        return SpawningResult.PASSED;
    }
}

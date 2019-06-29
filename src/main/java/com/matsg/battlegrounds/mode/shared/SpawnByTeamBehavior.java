package com.matsg.battlegrounds.mode.shared;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.game.Team;

public class SpawnByTeamBehavior implements SpawningBehavior {

    private Arena arena;

    public SpawnByTeamBehavior(Arena arena) {
        this.arena = arena;
    }

    public SpawningResult spawnPlayers(Iterable<GamePlayer> players) {
        if (arena.getSpawnCount() <= 0) {
            return new SpawningResult("No spawns were found in arena " + arena.getName());
        }

        for (GamePlayer gamePlayer : players) {
            Team team = gamePlayer.getTeam();
            Spawn spawn = arena.getTeamBase(team.getId());

            if (spawn == null) {
                if (arena.getSpawnCount(team.getId()) <= 0) {
                    return new SpawningResult("No spawns of team " + team.getId() + " were be found in arena " + arena.getName());
                }

                spawn = arena.getRandomSpawn(team.getId());
                spawn.setOccupant(team);
            }

            gamePlayer.getPlayer().teleport(spawn.getLocation());
        }

        return SpawningResult.PASSED;
    }
}

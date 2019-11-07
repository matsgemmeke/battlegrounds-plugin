package com.matsg.battlegrounds.mode.shared;

import com.matsg.battlegrounds.api.entity.GamePlayer;

public interface SpawningBehavior {

    /**
     * Executes the spawning logic.
     *
     * @param players the list of players to be spawned
     * @return the result of the execution of the spawning logic
     */
    SpawningResult spawnPlayers(Iterable<GamePlayer> players);
}

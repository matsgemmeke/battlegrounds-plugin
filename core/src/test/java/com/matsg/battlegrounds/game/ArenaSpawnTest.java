package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.SpawnOccupant;
import com.matsg.battlegrounds.entity.BattleGamePlayer;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ArenaSpawnTest {

    private World world;

    @Before
    public void setUp() {
        this.world = mock(World.class);
    }

    @Test
    public void createBasicSpawn() {
        boolean teamBase = true;
        int id = 1;
        int teamId = 2;
        Location location = new Location(world, 10.0, 10.0, 10.0);
        SpawnOccupant occupant = new BattleGamePlayer(null, null);

        ArenaSpawn spawn = new ArenaSpawn(id, location, teamId);
        spawn.setOccupant(occupant);
        spawn.setTeamBase(teamBase);

        assertEquals(id, spawn.getId());
        assertEquals(location, spawn.getLocation());
        assertEquals(occupant, spawn.getOccupant());
        assertEquals(teamBase, spawn.isTeamBase());
        assertEquals(teamId, spawn.getTeamId());
        assertTrue(spawn.isOccupied());
    }
}

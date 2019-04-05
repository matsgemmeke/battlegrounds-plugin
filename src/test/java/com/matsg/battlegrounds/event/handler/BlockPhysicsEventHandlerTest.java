package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.storage.BattlegroundsConfig;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.game.BattleArena;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ BattlegroundsConfig.class })
public class BlockPhysicsEventHandlerTest {

    private Arena arena;
    private Battlegrounds plugin;
    private Block block;
    private BlockPhysicsEvent event;
    private Game game;
    private GameManager gameManager;
    private World world;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.block = mock(Block.class);
        this.game = mock(Game.class);
        this.world = mock(World.class);

        this.arena = new BattleArena("Arena", world, new Location(world, 100, 100, 100), new Location(world, 0, 0, 0));
        this.event = new BlockPhysicsEvent(block, mock(BlockData.class));
        this.gameManager = new BattleGameManager();

        List<Arena> arenaList = new ArrayList<>();
        arenaList.add(arena);

        gameManager.getGames().add(game);

        when(block.getLocation()).thenReturn(new Location(world, 50, 50, 50));
        when(game.getArena()).thenReturn(arena);
        when(game.getArenaList()).thenReturn(arenaList);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testBlockPhysicsNotAllowedInsideArena() {
        BlockPhysicsEventHandler eventHandler = new BlockPhysicsEventHandler(plugin);
        eventHandler.handle(event);

        assertTrue(event.isCancelled());
    }

    @Test
    public void testBlockPhysicsAllowedOutsideArena() {
        when(block.getLocation()).thenReturn(new Location(world, 1000, 1000, 1000));

        BlockPhysicsEventHandler eventHandler = new BlockPhysicsEventHandler(plugin);
        eventHandler.handle(event);

        assertFalse(event.isCancelled());
    }
}

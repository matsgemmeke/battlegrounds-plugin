package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.config.BattlegroundsConfig;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.game.BattleArena;
import com.matsg.battlegrounds.player.BattleGamePlayer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ BattlegroundsConfig.class })
public class BlockBreakEventHandlerTest {

    private Arena arena;
    private Battlegrounds plugin;
    private BattlegroundsConfig config;
    private Block block;
    private BlockBreakEvent event;
    private Game game;
    private GameManager gameManager;
    private GamePlayer gamePlayer;
    private Player player;
    private PlayerManager playerManager;
    private World world;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.config = PowerMockito.mock(BattlegroundsConfig.class);
        this.block = mock(Block.class);
        this.game = mock(Game.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);
        this.world = mock(World.class);


        this.arena = new BattleArena("Arena", new Location(world, 100, 100, 100), new Location(world, 0, 0, 0), world);
        this.event = new BlockBreakEvent(block, player);
        this.gameManager = new BattleGameManager();
        this.gamePlayer = new BattleGamePlayer(player, null);

        List<Arena> arenaList = new ArrayList<>();
        arenaList.add(arena);

        gameManager.getGames().add(game);

        when(block.getLocation()).thenReturn(new Location(world, 50, 50, 50));
        when(game.getArena()).thenReturn(arena);
        when(game.getArenaList()).thenReturn(arenaList);
        when(game.getPlayerManager()).thenReturn(playerManager);
        when(plugin.getBattlegroundsConfig()).thenReturn(config);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testBlockBreakOutsideArena() {
        config.arenaProtection = true;

        when(block.getLocation()).thenReturn(new Location(world, 1000, 1000, 1000));

        BlockBreakEventHandler eventHandler = new BlockBreakEventHandler(plugin);
        eventHandler.handle(event);

        assertFalse(event.isCancelled());
    }

    @Test
    public void testBlockBreakWithoutArenaProtectionWhenNotPlaying() {
        config.arenaProtection = false;

        when(playerManager.getGamePlayer(player)).thenReturn(null);

        BlockBreakEventHandler eventHandler = new BlockBreakEventHandler(plugin);
        eventHandler.handle(event);

        assertFalse(event.isCancelled());
    }

    @Test
    public void testBlockBreakWithArenaProtectionWhenNotPlaying() {
        config.arenaProtection = true;

        when(playerManager.getGamePlayer(player)).thenReturn(null);

        BlockBreakEventHandler eventHandler = new BlockBreakEventHandler(plugin);
        eventHandler.handle(event);

        assertTrue(event.isCancelled());
    }

    @Test
    public void testBlockBreakWithoutArenaProtectionWhenPlaying() {
        config.arenaProtection = false;

        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        BlockBreakEventHandler eventHandler = new BlockBreakEventHandler(plugin);
        eventHandler.handle(event);

        assertTrue(event.isCancelled());
    }

    @Test
    public void testBlockBreakWithArenaProtectionWhenPlaying() {
        config.arenaProtection = true;

        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        BlockBreakEventHandler eventHandler = new BlockBreakEventHandler(plugin);
        eventHandler.handle(event);

        assertTrue(event.isCancelled());
    }
}

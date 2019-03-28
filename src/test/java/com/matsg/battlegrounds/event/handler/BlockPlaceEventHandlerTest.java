package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.storage.BattlegroundsConfig;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.game.BattleArena;
import com.matsg.battlegrounds.entity.BattleGamePlayer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ BattlegroundsConfig.class, BlockPlaceEvent.class })
public class BlockPlaceEventHandlerTest {

    private Arena arena;
    private Battlegrounds plugin;
    private BattlegroundsConfig config;
    private Block block;
    private BlockPlaceEvent event;
    private Game game;
    private GameManager gameManager;
    private GamePlayer gamePlayer;
    private Player player;
    private PlayerManager playerManager;
    private World world;

    @Before
    public void setUp() {
        this.arena = mock(Arena.class);
        this.plugin = mock(Battlegrounds.class);
        this.config = PowerMockito.mock(BattlegroundsConfig.class);
        this.block = mock(Block.class);
        this.event = PowerMockito.mock(BlockPlaceEvent.class);
        this.game = mock(Game.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);
        this.world = mock(World.class);

        this.arena = new BattleArena("Arena", new Location(world, 100, 100, 100), new Location(world, 0, 0, 0), world);
        this.gameManager = new BattleGameManager();
        this.gamePlayer = new BattleGamePlayer(player, null);

        List<Arena> arenaList = new ArrayList<>();
        arenaList.add(arena);

        gameManager.getGames().add(game);

        when(block.getLocation()).thenReturn(new Location(world, 50, 50, 50));
        when(event.getBlock()).thenReturn(block);
        when(event.getPlayer()).thenReturn(player);
        when(game.getArena()).thenReturn(arena);
        when(game.getArenaList()).thenReturn(arenaList);
        when(game.getPlayerManager()).thenReturn(playerManager);
        when(plugin.getBattlegroundsConfig()).thenReturn(config);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testBlockPlaceOutsideArena() {
        config.arenaProtection = true;

        when(block.getLocation()).thenReturn(new Location(world, 1000, 1000, 1000));

        BlockPlaceEventHandler eventHandler = new BlockPlaceEventHandler(plugin);
        eventHandler.handle(event);

        verify(event, times(0)).setCancelled(true);
    }

    @Test
    public void testBlockPlaceWithoutArenaProtectionWhenNotPlaying() {
        config.arenaProtection = false;

        when(playerManager.getGamePlayer(player)).thenReturn(null);

        BlockPlaceEventHandler eventHandler = new BlockPlaceEventHandler(plugin);
        eventHandler.handle(event);

        verify(event, times(0)).setCancelled(true);
    }

    @Test
    public void testBlockPlaceWithArenaProtectionWhenNotPlaying() {
        config.arenaProtection = true;

        when(playerManager.getGamePlayer(player)).thenReturn(null);

        BlockPlaceEventHandler eventHandler = new BlockPlaceEventHandler(plugin);
        eventHandler.handle(event);

        verify(event, times(1)).setCancelled(true);
    }

    @Test
    public void testBlockPlaceWithoutArenaProtectionWhenPlaying() {
        config.arenaProtection = false;

        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        BlockPlaceEventHandler eventHandler = new BlockPlaceEventHandler(plugin);
        eventHandler.handle(event);

        verify(event, times(1)).setCancelled(true);
    }

    @Test
    public void testBlockPlaceWithArenaProtectionWhenPlaying() {
        config.arenaProtection = true;

        when(playerManager.getGamePlayer(player)).thenReturn(null);

        BlockPlaceEventHandler eventHandler = new BlockPlaceEventHandler(plugin);
        eventHandler.handle(event);

        verify(event, times(1)).setCancelled(true);
    }
}

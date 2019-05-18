package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.ItemRegistry;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.item.Attachment;
import com.matsg.battlegrounds.api.item.Firearm;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.game.state.InGameState;
import com.matsg.battlegrounds.game.state.ResettingState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlayerDropItemEventHandlerTest {

    private Battlegrounds plugin;
    private Game game;
    private GameManager gameManager;
    private GamePlayer gamePlayer;
    private org.bukkit.entity.Item itemEntity;
    private ItemRegistry itemRegistry;
    private ItemStack itemStack;
    private Player player;
    private PlayerDropItemEvent event;
    private PlayerManager playerManager;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.game = mock(Game.class);
        this.gamePlayer = mock(GamePlayer.class);
        this.itemEntity = mock(org.bukkit.entity.Item.class);
        this.itemRegistry = mock(ItemRegistry.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);

        this.event = new PlayerDropItemEvent(player, itemEntity);
        this.gameManager = new BattleGameManager();
        this.itemStack = new ItemStack(Material.IRON_HOE);

        gameManager.getGames().add(game);

        when(game.getItemRegistry()).thenReturn(itemRegistry);
        when(game.getPlayerManager()).thenReturn(playerManager);
        when(itemEntity.getItemStack()).thenReturn(itemStack);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testPlayerItemDropWhenNotPlaying() {
        when(playerManager.getGamePlayer(player)).thenReturn(null);

        PlayerDropItemEventHandler eventHandler = new PlayerDropItemEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(1)).getGamePlayer(player);

        assertFalse(event.isCancelled());
    }

    @Test
    public void testPlayerItemDropWhenNotHoldingItem() {
        when(itemRegistry.getItemIgnoreMetadata(itemStack)).thenReturn(null);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        PlayerDropItemEventHandler eventHandler = new PlayerDropItemEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(2)).getGamePlayer(player);

        assertTrue(event.isCancelled());
    }

    @Test
    public void testPlayerItemDropWhenGameDoesNotAllowItems() {
        Firearm firearm = mock(Firearm.class);

        when(game.getState()).thenReturn(new ResettingState());
        when(itemRegistry.getItem(itemStack)).thenReturn(firearm);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        PlayerDropItemEventHandler eventHandler = new PlayerDropItemEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(2)).getGamePlayer(player);

        assertTrue(event.isCancelled());
    }

    @Test
    public void testPlayerItemDropWhenHoldingNonDroppableItem() {
        Attachment attachment = mock(Attachment.class);

        when(game.getState()).thenReturn(new InGameState());
        when(itemRegistry.getItemIgnoreMetadata(itemStack)).thenReturn(attachment);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        PlayerDropItemEventHandler eventHandler = new PlayerDropItemEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(2)).getGamePlayer(player);

        assertTrue(event.isCancelled());
    }

    @Test
    public void testPlayerItemDropWhenHoldinDroppableItem() {
        Firearm firearm = mock(Firearm.class);

        when(firearm.onDrop(gamePlayer, itemEntity)).thenReturn(true);
        when(game.getState()).thenReturn(new InGameState());
        when(itemRegistry.getItemIgnoreMetadata(itemStack)).thenReturn(firearm);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        PlayerDropItemEventHandler eventHandler = new PlayerDropItemEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(2)).getGamePlayer(player);

        assertTrue(event.isCancelled());
    }
}

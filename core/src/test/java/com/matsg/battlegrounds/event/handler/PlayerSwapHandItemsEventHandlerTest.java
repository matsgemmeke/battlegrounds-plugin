package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.ItemRegistry;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlayerSwapHandItemsEventHandlerTest {

    private Battlegrounds plugin;
    private Game game;
    private GameManager gameManager;
    private GamePlayer gamePlayer;
    private Item item;
    private ItemRegistry itemRegistry;
    private ItemStack itemStack;
    private Player player;
    private PlayerManager playerManager;
    private PlayerSwapHandItemsEvent event;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.game = mock(Game.class);
        this.gamePlayer = mock(GamePlayer.class);
        this.item = mock(Item.class);
        this.itemRegistry = mock(ItemRegistry.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);

        this.itemStack = new ItemStack(Material.IRON_HOE);
        this.event = new PlayerSwapHandItemsEvent(player, null, itemStack);
        this.gameManager = new BattleGameManager();

        gameManager.getGames().add(game);

        when(game.getItemRegistry()).thenReturn(itemRegistry);
        when(game.getPlayerManager()).thenReturn(playerManager);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void playerSwapItemsWhenNotPlaying() {
        when(playerManager.getGamePlayer(player)).thenReturn(null);

        PlayerSwapHandItemsEventHandler eventHandler = new PlayerSwapHandItemsEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(1)).getGamePlayer(player);

        assertFalse(event.isCancelled());
    }

    @Test
    public void playerSwapItemsWhenNotHoldingItem() {
        when(itemRegistry.getItem(itemStack)).thenReturn(null);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        PlayerSwapHandItemsEventHandler eventHandler = new PlayerSwapHandItemsEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(1)).getGamePlayer(player);

        assertTrue(event.isCancelled());
    }

    @Test
    public void playerSwapItemsWhenHoldingItem() {
        when(itemRegistry.getItem(itemStack)).thenReturn(item);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        PlayerSwapHandItemsEventHandler eventHandler = new PlayerSwapHandItemsEventHandler(plugin);
        eventHandler.handle(event);

        verify(item, times(1)).onSwap(gamePlayer, event);
        verify(playerManager, times(2)).getGamePlayer(player);

        assertTrue(event.isCancelled());
    }
}

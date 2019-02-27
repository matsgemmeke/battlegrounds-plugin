package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.ItemRegistry;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.item.Attachment;
import com.matsg.battlegrounds.api.item.Equipment;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class PlayerPickUpItemEventHandlerTest {

    private Battlegrounds plugin;
    private Game game;
    private GameManager gameManager;
    private GamePlayer gamePlayer;
    private org.bukkit.entity.Item itemEntity;
    private ItemRegistry itemRegistry;
    private ItemStack itemStack;
    private Player player;
    private PlayerManager playerManager;
    private PlayerPickupItemEvent event;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.game = mock(Game.class);
        this.gamePlayer = mock(GamePlayer.class);
        this.itemEntity = mock(org.bukkit.entity.Item.class);
        this.itemRegistry = mock(ItemRegistry.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);

        this.event = new PlayerPickupItemEvent(player, itemEntity, 0);
        this.gameManager = new BattleGameManager();
        this.itemStack = new ItemStack(Material.IRON_HOE);

        gameManager.getGames().add(game);

        when(game.getItemRegistry()).thenReturn(itemRegistry);
        when(game.getPlayerManager()).thenReturn(playerManager);
        when(itemEntity.getItemStack()).thenReturn(itemStack);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testPlayerPickUpItemWhenNotPlaying() {
        when(playerManager.getGamePlayer(player)).thenReturn(null);

        PlayerPickupItemEventHandler eventHandler = new PlayerPickupItemEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(1)).getGamePlayer(player);

        assertFalse(event.isCancelled());
    }

    @Test
    public void testPlayerPickUpItemNotDroppable() {
        Item item = mock(Attachment.class);
        List<Item> items = new ArrayList<>();

        items.add(item);

        when(itemRegistry.getItems()).thenReturn(items);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        PlayerPickupItemEventHandler eventHandler = new PlayerPickupItemEventHandler(plugin);
        eventHandler.handle(event);

        verify(itemRegistry, times(1)).getItems();
        verify(playerManager, times(1)).getGamePlayer(player);

        assertTrue(event.isCancelled());
    }

    @Test
    public void testPlayerPickUpItemIsDroppable() {
        Equipment equipment = mock(Equipment.class);
        List<Item> items = new ArrayList<>();

        items.add(equipment);

        when(equipment.isRelated(itemStack)).thenReturn(true);
        when(equipment.onPickUp(gamePlayer, itemEntity)).thenReturn(true);
        when(itemRegistry.getItems()).thenReturn(items);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        PlayerPickupItemEventHandler eventHandler = new PlayerPickupItemEventHandler(plugin);
        eventHandler.handle(event);

        verify(itemRegistry, times(1)).getItems();
        verify(playerManager, times(2)).getGamePlayer(player);

        assertTrue(event.isCancelled());
    }
}

package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlayerItemHeldEventHandlerTest {

    private Battlegrounds plugin;
    private Game game;
    private GameManager gameManager;
    private GamePlayer gamePlayer;
    private Loadout loadout;
    private Player player;
    private PlayerItemHeldEvent event;
    private PlayerManager playerManager;
    private Weapon weapon;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.game = mock(Game.class);
        this.gamePlayer = mock(GamePlayer.class);
        this.loadout = mock(Loadout.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);
        this.weapon = mock(Weapon.class);

        this.event = new PlayerItemHeldEvent(player, 0, 1);
        this.gameManager = new BattleGameManager();

        gameManager.getGames().add(game);

        when(game.getPlayerManager()).thenReturn(playerManager);
        when(gamePlayer.getLoadout()).thenReturn(loadout);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testItemSwitchWhenNotPlaying() {
        when(playerManager.getGamePlayer(player)).thenReturn(null);

        PlayerItemHeldEventHandler eventHandler = new PlayerItemHeldEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(1)).getGamePlayer(player);

        assertFalse(event.isCancelled());
    }

    @Test
    public void testItemSwitchWhenHavingNoLoadoutSelected() {
        when(gamePlayer.getLoadout()).thenReturn(null);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        PlayerItemHeldEventHandler eventHandler = new PlayerItemHeldEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(2)).getGamePlayer(player);

        assertFalse(event.isCancelled());
    }

    @Test
    public void testItemSwitchWhenNotHoldingWeapon() {
        when(loadout.getWeapon(any(ItemStack.class))).thenReturn(null);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        PlayerItemHeldEventHandler eventHandler = new PlayerItemHeldEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(2)).getGamePlayer(player);
        verify(weapon, times(0)).onSwitch(gamePlayer);

        assertFalse(event.isCancelled());
    }

    @Test
    public void testItemSwitchWhenHoldingWeapon() {
        when(loadout.getWeapon(any(ItemStack.class))).thenReturn(weapon);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        PlayerItemHeldEventHandler eventHandler = new PlayerItemHeldEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(2)).getGamePlayer(player);
        verify(weapon, times(1)).onSwitch(gamePlayer);

        assertFalse(event.isCancelled());
    }
}

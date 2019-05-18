package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.item.MeleeWeapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.entity.BattleGamePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EntityDamageByEntityEventHandlerTest {

    private Battlegrounds plugin;
    private EntityDamageByEntityEvent event;
    private Game game;
    private GameManager gameManager;
    private GamePlayer damager, gamePlayer;
    private Player damagerPlayer, player;
    private PlayerManager playerManager;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.damagerPlayer = mock(Player.class);
        this.game = mock(Game.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);

        this.damager = new BattleGamePlayer(damagerPlayer, null);
        this.event = new EntityDamageByEntityEvent(damagerPlayer, player, null, 0);
        this.gameManager = new BattleGameManager();
        this.gamePlayer = new BattleGamePlayer(player, null);

        gameManager.getGames().add(game);

        when(game.getPlayerManager()).thenReturn(playerManager);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testPlayerCheck() {
        event = new EntityDamageByEntityEvent(mock(Entity.class), mock(Entity.class), null, 10.0);

        EntityDamageByEntityEventHandler eventHandler = new EntityDamageByEntityEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(0)).getGamePlayer(player);

        assertFalse(event.isCancelled());
    }

    @Test
    public void testPlayerDamageWhenNotPlaying() {
        when(playerManager.getGamePlayer(player)).thenReturn(null);

        EntityDamageByEntityEventHandler eventHandler = new EntityDamageByEntityEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(1)).getGamePlayer(player);

        assertFalse(event.isCancelled());
    }

    @Test
    public void testPlayerDamageWithoutLoadout() {
        when(playerManager.getGamePlayer(damagerPlayer)).thenReturn(damager);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        damager.setLoadout(null);

        EntityDamageByEntityEventHandler eventHandler = new EntityDamageByEntityEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(3)).getGamePlayer(any(Player.class));

        assertTrue(event.isCancelled());
    }

    @Test
    public void testPlayerDamageSameTeam() {
        when(playerManager.getGamePlayer(damagerPlayer)).thenReturn(damager);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        Team team = new BattleTeam(1, "Team", null, null);

        damager.setTeam(team);
        gamePlayer.setTeam(team);

        EntityDamageByEntityEventHandler eventHandler = new EntityDamageByEntityEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(3)).getGamePlayer(any(Player.class));

        assertTrue(event.isCancelled());
    }

    @Test
    public void testPlayerDamageWithoutMeleeWeapon() {
        when(playerManager.getGamePlayer(damagerPlayer)).thenReturn(damager);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        Team team = new BattleTeam(1, "Team", null, null), enemyTeam = new BattleTeam(2, "Team", null, null);

        damager.setLoadout(mock(Loadout.class));
        damager.setTeam(enemyTeam);
        gamePlayer.setTeam(team);

        EntityDamageByEntityEventHandler eventHandler = new EntityDamageByEntityEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(3)).getGamePlayer(any(Player.class));

        assertTrue(event.isCancelled());
    }

    @Test
    public void testPlayerDamageWithMeleeWeapon() {
        when(playerManager.getGamePlayer(damagerPlayer)).thenReturn(damager);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        Loadout loadout = mock(Loadout.class);
        MeleeWeapon meleeWeapon = mock(MeleeWeapon.class);
        Team team = new BattleTeam(1, "Team", null, null), enemyTeam = new BattleTeam(2, "Team", null, null);

        when(loadout.getWeapon(any(ItemSlot.class))).thenReturn(meleeWeapon);
        when(loadout.getWeapon(any(ItemStack.class))).thenReturn(meleeWeapon);

        damager.setLoadout(loadout);
        damager.setTeam(enemyTeam);
        gamePlayer.setTeam(team);

        EntityDamageByEntityEventHandler eventHandler = new EntityDamageByEntityEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(3)).getGamePlayer(any(Player.class));

        assertEquals(0.0, event.getDamage(), 0);
        assertFalse(event.isCancelled());
    }
}

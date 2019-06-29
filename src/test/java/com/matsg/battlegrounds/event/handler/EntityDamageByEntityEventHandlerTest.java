package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.MobManager;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.item.MeleeWeapon;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.entity.BattleGamePlayer;
import org.bukkit.Material;
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
    private Entity entity;
    private Game game;
    private GameManager gameManager;
    private GamePlayer damager;
    private MobManager mobManager;
    private Player player;
    private PlayerManager playerManager;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.entity = mock(Entity.class);
        this.game = mock(Game.class);
        this.mobManager = mock(MobManager.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);

        this.damager = new BattleGamePlayer(player, null);
        this.gameManager = new BattleGameManager();

        gameManager.getGames().add(game);

        when(game.getMobManager()).thenReturn(mobManager);
        when(game.getPlayerManager()).thenReturn(playerManager);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void entityDamageNotByAnotherPlayer() {
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(entity, entity, null, 10.0);

        EntityDamageByEntityEventHandler eventHandler = new EntityDamageByEntityEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(0)).getGamePlayer(player);

        assertFalse(event.isCancelled());
    }

    @Test
    public void entityDamageWhenNotPlaying() {
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, entity, null, 10.0);

        when(playerManager.getGamePlayer(player)).thenReturn(null);

        EntityDamageByEntityEventHandler eventHandler = new EntityDamageByEntityEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(1)).getGamePlayer(player);

        assertFalse(event.isCancelled());
    }

    @Test
    public void entityDamageWithUnknownPlayerOrMob() {
        Player fakePlayer = mock(Player.class);
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, fakePlayer, null, 10.0);

        when(mobManager.findMob(fakePlayer)).thenReturn(null);
        when(playerManager.getGamePlayer(fakePlayer)).thenReturn(null);
        when(playerManager.getGamePlayer(player)).thenReturn(damager);

        EntityDamageByEntityEventHandler eventHandler = new EntityDamageByEntityEventHandler(plugin);
        eventHandler.handle(event);

        assertTrue(event.isCancelled());
    }

    @Test
    public void entityDamageWhenDamagerHasNoLoadout() {
        Mob mob = mock(Mob.class);
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, entity, null, 10.0);

        when(mobManager.findMob(entity)).thenReturn(mob);
        when(playerManager.getGamePlayer(player)).thenReturn(damager);

        damager.setLoadout(null);

        EntityDamageByEntityEventHandler eventHandler = new EntityDamageByEntityEventHandler(plugin);
        eventHandler.handle(event);

        assertTrue(event.isCancelled());
    }

    @Test
    public void entityDamageWhenEntityIsNotHostile() {
        Loadout loadout = mock(Loadout.class);
        Mob mob = mock(Mob.class);
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, entity, null, 10.0);

        when(mob.isHostileTowards(damager)).thenReturn(false);
        when(mobManager.findMob(entity)).thenReturn(mob);
        when(playerManager.getGamePlayer(player)).thenReturn(damager);

        damager.setLoadout(loadout);

        EntityDamageByEntityEventHandler eventHandler = new EntityDamageByEntityEventHandler(plugin);
        eventHandler.handle(event);

        assertTrue(event.isCancelled());
    }

    @Test
    public void entityDamageWhenWeaponIsNotInstanceOfMeleeWeapon() {
        ItemStack itemStack = new ItemStack(Material.AIR);
        Loadout loadout = mock(Loadout.class);
        Mob mob = mock(Mob.class);
        Weapon weapon = mock(Weapon.class);

        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, entity, null, 10.0);

        when(loadout.getWeapon(itemStack)).thenReturn(weapon);
        when(mob.isHostileTowards(damager)).thenReturn(true);
        when(mobManager.findMob(entity)).thenReturn(mob);
        when(player.getItemInHand()).thenReturn(itemStack);
        when(playerManager.getGamePlayer(player)).thenReturn(damager);

        damager.setLoadout(loadout);

        EntityDamageByEntityEventHandler eventHandler = new EntityDamageByEntityEventHandler(plugin);
        eventHandler.handle(event);

        assertTrue(event.isCancelled());
    }

    @Test
    public void entityDamageWithMeleeWeapon() {
        ItemStack itemStack = new ItemStack(Material.AIR);
        Loadout loadout = mock(Loadout.class);
        MeleeWeapon meleeWeapon = mock(MeleeWeapon.class);
        Mob mob = mock(Mob.class);

        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, entity, null, 10.0);

        when(loadout.getWeapon(itemStack)).thenReturn(meleeWeapon);
        when(mob.isHostileTowards(damager)).thenReturn(true);
        when(mobManager.findMob(entity)).thenReturn(mob);
        when(player.getItemInHand()).thenReturn(itemStack);
        when(playerManager.getGamePlayer(player)).thenReturn(damager);

        damager.setLoadout(loadout);

        EntityDamageByEntityEventHandler eventHandler = new EntityDamageByEntityEventHandler(plugin);
        eventHandler.handle(event);

        assertEquals(event.getDamage(), 0.0, 0.01);
        assertFalse(event.isCancelled());
    }
}

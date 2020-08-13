package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.ItemRegistry;
import com.matsg.battlegrounds.api.item.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LoadoutFactoryTest {

    private Attachment attachment;
    private Equipment equipment;
    private Firearm primary;
    private Firearm secondary;
    private Game game;
    private GamePlayer gamePlayer;
    private ItemRegistry itemRegistry;
    private MeleeWeapon meleeWeapon;

    @Before
    public void setUp() {
        this.attachment = mock(Attachment.class);
        this.equipment = mock(Equipment.class);
        this.primary = mock(Firearm.class);
        this.secondary = mock(Firearm.class);
        this.game = mock(Game.class);
        this.gamePlayer = mock(GamePlayer.class);
        this.itemRegistry = mock(ItemRegistry.class);
        this.meleeWeapon = mock(MeleeWeapon.class);

        when(game.getItemRegistry()).thenReturn(itemRegistry);
    }

    @Test
    public void makeLoadoutWithoutWeapons() {
        LoadoutFactory loadoutFactory = new LoadoutFactory();
        Loadout loadout = loadoutFactory.make(1, "loadout", null, null, null, null, new Attachment[0], new Attachment[0], game, gamePlayer, false);

        assertEquals(1, loadout.getLoadoutNr());
        assertEquals("loadout", loadout.getName());
        assertNull(loadout.getPrimary());
        assertNull(loadout.getSecondary());
        assertNull(loadout.getEquipment());
        assertNull(loadout.getMeleeWeapon());
    }

    @Test
    public void makeLoadoutWithPrimaryFirearm() {
        ItemSlot itemSlot = ItemSlot.FIREARM_PRIMARY;

        LoadoutFactory loadoutFactory = new LoadoutFactory();
        Loadout loadout = loadoutFactory.make(1, "loadout", primary, null, null, null, new Attachment[0], new Attachment[0], game, gamePlayer, false);

        verify(primary, times(1)).setGame(game);
        verify(primary, times(1)).setGamePlayer(gamePlayer);
        verify(primary, times(1)).setItemSlot(itemSlot);

        assertEquals(primary, loadout.getPrimary());
    }

    @Test
    public void makeLoadoutWithSecondaryFirearm() {
        ItemSlot itemSlot = ItemSlot.FIREARM_SECONDARY;

        LoadoutFactory loadoutFactory = new LoadoutFactory();
        Loadout loadout = loadoutFactory.make(1, "loadout", null, secondary, null, null, new Attachment[0], new Attachment[0], game, gamePlayer, false);

        verify(secondary, times(1)).setGame(game);
        verify(secondary, times(1)).setGamePlayer(gamePlayer);
        verify(secondary, times(1)).setItemSlot(itemSlot);

        assertEquals(secondary, loadout.getSecondary());
    }

    @Test
    public void makeLoadoutWithEquipment() {
        ItemSlot itemSlot = ItemSlot.EQUIPMENT;

        LoadoutFactory loadoutFactory = new LoadoutFactory();
        Loadout loadout = loadoutFactory.make(1, "loadout", null, null, equipment, null, new Attachment[0], new Attachment[0], game, gamePlayer, false);

        verify(equipment, times(1)).setGame(game);
        verify(equipment, times(1)).setGamePlayer(gamePlayer);
        verify(equipment, times(1)).setItemSlot(itemSlot);

        assertEquals(equipment, loadout.getEquipment());
    }

    @Test
    public void makeLoadoutWithMeleeWeapon() {
        ItemSlot itemSlot = ItemSlot.MELEE_WEAPON;

        LoadoutFactory loadoutFactory = new LoadoutFactory();
        Loadout loadout = loadoutFactory.make(1, "loadout", null, null, null, meleeWeapon, new Attachment[0], new Attachment[0], game, gamePlayer, false);

        verify(meleeWeapon, times(1)).setGame(game);
        verify(meleeWeapon, times(1)).setGamePlayer(gamePlayer);
        verify(meleeWeapon, times(1)).setItemSlot(itemSlot);

        assertEquals(meleeWeapon, loadout.getMeleeWeapon());
    }

    @Test
    public void makeLoadoutWithPrimaryAttachments() {
        Attachment[] attachments = new Attachment[] { attachment };
        Gun primary = mock(Gun.class);
        List<Attachment> list = new ArrayList<>();

        when(primary.getAttachments()).thenReturn(list);

        LoadoutFactory loadoutFactory = new LoadoutFactory();
        loadoutFactory.make(1, "loadout", primary, null, null, meleeWeapon, attachments, new Attachment[0], game, gamePlayer, false);

        assertEquals(1, list.size());
    }

    @Test
    public void makeLoadoutWithSecondaryAttachments() {
        Attachment[] attachments = new Attachment[] { attachment };
        Gun secondary = mock(Gun.class);
        List<Attachment> list = new ArrayList<>();

        when(secondary.getAttachments()).thenReturn(list);

        LoadoutFactory loadoutFactory = new LoadoutFactory();
        loadoutFactory.make(1, "loadout", null, secondary, null, meleeWeapon, new Attachment[0], attachments, game, gamePlayer, false);

        assertEquals(1, list.size());
    }
}

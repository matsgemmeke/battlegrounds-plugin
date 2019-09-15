package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.storage.ItemConfig;
import com.matsg.battlegrounds.api.item.MeleeWeapon;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        BattlegroundsPlugin.class,
        Bukkit.class
})
public class MeleeWeaponFactoryTest {

    private ConfigurationSection section;
    private EventDispatcher eventDispatcher;
    private ItemConfig meleeWeaponConfig;
    private String id;
    private Translator translator;

    @Before
    public void setUp() {
        this.section = mock(ConfigurationSection.class);
        this.eventDispatcher = mock(EventDispatcher.class);
        this.meleeWeaponConfig = mock(ItemConfig.class);
        this.translator = mock(Translator.class);

        this.id = "Id";

        PowerMockito.mockStatic(BattlegroundsPlugin.class);
        PowerMockito.mockStatic(Bukkit.class);

        ItemFactory itemFactory = mock(ItemFactory.class);
        ItemMeta itemMeta = mock(ItemMeta.class, withSettings().extraInterfaces(Damageable.class)); // Add the Damageable interface so the ItemMeta can be casted when setting the durability

        when(Bukkit.getItemFactory()).thenReturn(itemFactory);
        when(itemFactory.getItemMeta(any())).thenReturn(itemMeta);
        when(meleeWeaponConfig.getItemConfigurationSection(id)).thenReturn(section);
        when(section.getString("Material")).thenReturn("AIR,1");
    }

    @Test
    public void makeMeleeWeapon() {
        when(section.getInt("Amount")).thenReturn(1);
        when(section.getInt("Cooldown")).thenReturn(1);
        when(section.getDouble("Damage")).thenReturn(10.0);
        when(section.getName()).thenReturn(id);

        MeleeWeaponFactory factory = new MeleeWeaponFactory(meleeWeaponConfig, eventDispatcher, translator);
        MeleeWeapon meleeWeapon = factory.make(id);

        assertNotNull(meleeWeapon);
        assertEquals(id, meleeWeapon.getMetadata().getId());
    }

    @Test(expected = FactoryCreationException.class)
    public void makeMeleeWeaponWithFailingValidation() {
        when(section.getInt("Amount")).thenReturn(-1);
        when(section.getInt("Cooldown")).thenReturn(-1);
        when(section.getDouble("Damage")).thenReturn(-10.0);

        MeleeWeaponFactory factory = new MeleeWeaponFactory(meleeWeaponConfig, eventDispatcher, translator);
        factory.make(id);
    }
}

package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.Translator;
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
        Bukkit.class,
        Translator.class
})
public class MeleeWeaponFactoryTest {

    private ConfigurationSection section;
    private ItemConfig meleeWeaponConfig;
    private String id;

    @Before
    public void setUp() {
        this.section = mock(ConfigurationSection.class);
        this.meleeWeaponConfig = mock(ItemConfig.class);

        this.id = "Id";

        PowerMockito.mockStatic(Bukkit.class);
        PowerMockito.mockStatic(Translator.class);

        ItemFactory itemFactory = mock(ItemFactory.class);
        ItemMeta itemMeta = mock(ItemMeta.class, withSettings().extraInterfaces(Damageable.class)); // Add the Damageable interface so the ItemMeta can be casted when setting the durability

        when(Bukkit.getItemFactory()).thenReturn(itemFactory);
        when(itemFactory.getItemMeta(any())).thenReturn(itemMeta);
        when(meleeWeaponConfig.getItemConfigurationSection(id)).thenReturn(section);
        when(section.getString("Material")).thenReturn("AIR,1");
    }

    @Test
    public void testMakeMeleeWeapon() {
        when(section.getInt("Amount")).thenReturn(1);
        when(section.getInt("Cooldown")).thenReturn(1);
        when(section.getDouble("Damage")).thenReturn(10.0);
        when(section.getName()).thenReturn(id);

        MeleeWeaponFactory factory = new MeleeWeaponFactory(meleeWeaponConfig);
        MeleeWeapon meleeWeapon = factory.make(id);

        assertNotNull(meleeWeapon);
        assertEquals(id, meleeWeapon.getId());
    }

    @Test(expected = FactoryCreationException.class)
    public void testMakeMeleeWeaponWithFailingValidation() {
        when(section.getInt("Amount")).thenReturn(-1);
        when(section.getInt("Cooldown")).thenReturn(-1);
        when(section.getDouble("Damage")).thenReturn(-10.0);

        MeleeWeaponFactory factory = new MeleeWeaponFactory(meleeWeaponConfig);
        factory.make(id);
    }
}

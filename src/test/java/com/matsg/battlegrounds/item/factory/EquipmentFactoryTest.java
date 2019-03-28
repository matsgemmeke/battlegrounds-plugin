package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.Translator;
import com.matsg.battlegrounds.api.storage.ItemConfig;
import com.matsg.battlegrounds.api.item.Equipment;
import com.matsg.battlegrounds.api.item.Lethal;
import com.matsg.battlegrounds.api.item.Tactical;
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
public class EquipmentFactoryTest {

    private ConfigurationSection section;
    private ItemConfig equipmentConfig;
    private String id;

    @Before
    public void setUp() {
        this.section = mock(ConfigurationSection.class);
        this.equipmentConfig = mock(ItemConfig.class);

        this.id = "Id";

        PowerMockito.mockStatic(Bukkit.class);
        PowerMockito.mockStatic(Translator.class);

        ItemFactory itemFactory = mock(ItemFactory.class);
        ItemMeta itemMeta = mock(ItemMeta.class, withSettings().extraInterfaces(Damageable.class)); // Add the Damageable interface so the ItemMeta can be casted when setting the durability

        when(Bukkit.getItemFactory()).thenReturn(itemFactory);
        when(itemFactory.getItemMeta(any())).thenReturn(itemMeta);
        when(equipmentConfig.getItemConfigurationSection(id)).thenReturn(section);
        when(section.getString("Material")).thenReturn("AIR,1");
        when(Translator.translate(anyString())).thenReturn("test");
    }

    @Test(expected = FactoryCreationException.class)
    public void testMakeEquipmentInvalidType() {
        when(section.getString("EquipmentType")).thenReturn("INVALID");

        EquipmentFactory factory = new EquipmentFactory(equipmentConfig);
        factory.make(id);
    }

    @Test(expected = FactoryCreationException.class)
    public void testMakeLethalWithFailingValidation() {
        when(section.getInt("Amount")).thenReturn(-1);
        when(section.getString("EquipmentType")).thenReturn("LETHAL");

        EquipmentFactory factory = new EquipmentFactory(equipmentConfig);
        factory.make(id);
    }

    @Test(expected = FactoryCreationException.class)
    public void testMakeTacticalWithFailingValidation() {
        when(section.getInt("Amount")).thenReturn(-1);
        when(section.getString("EquipmentType")).thenReturn("TACTICAL");

        EquipmentFactory factory = new EquipmentFactory(equipmentConfig);
        factory.make(id);
    }

    @Test
    public void testMakeLethal() {
        when(section.getInt("Amount")).thenReturn(1);
        when(section.getInt("Cooldown")).thenReturn(1);
        when(section.getString("EquipmentType")).thenReturn("LETHAL");
        when(section.getInt("IgnitionTime")).thenReturn(1);
        when(section.getDouble("Range.Long.Damage")).thenReturn(1.0);
        when(section.getDouble("Range.Long.Distance")).thenReturn(1.0);
        when(section.getDouble("Range.Medium.Damage")).thenReturn(1.0);
        when(section.getDouble("Range.Medium.Distance")).thenReturn(1.0);
        when(section.getDouble("Range.Short.Damage")).thenReturn(1.0);
        when(section.getDouble("Range.Short.Distance")).thenReturn(1.0);
        when(section.getDouble("Velocity")).thenReturn(1.0);
        when(section.getName()).thenReturn(id);

        EquipmentFactory factory = new EquipmentFactory(equipmentConfig);
        Equipment equipment = factory.make(id);

        assertNotNull(equipment);
        assertEquals(id, equipment.getId());
        assertTrue(equipment instanceof Lethal);
    }

    @Test
    public void testMakeTactical() {
        when(section.getInt("Amount")).thenReturn(1);
        when(section.getInt("Cooldown")).thenReturn(1);
        when(section.getInt("Duration")).thenReturn(1);
        when(section.getString("Effect")).thenReturn("BLINDNESS");
        when(section.getString("EquipmentType")).thenReturn("TACTICAL");
        when(section.getInt("IgnitionTime")).thenReturn(1);
        when(section.getDouble("Range.Long.Distance")).thenReturn(1.0);
        when(section.getDouble("Range.Medium.Distance")).thenReturn(1.0);
        when(section.getDouble("Range.Short.Distance")).thenReturn(1.0);
        when(section.getDouble("Velocity")).thenReturn(1.0);
        when(section.getName()).thenReturn(id);

        EquipmentFactory factory = new EquipmentFactory(equipmentConfig);
        Equipment equipment = factory.make(id);

        assertNotNull(equipment);
        assertEquals(id, equipment.getId());
        assertTrue(equipment instanceof Tactical);
    }
}

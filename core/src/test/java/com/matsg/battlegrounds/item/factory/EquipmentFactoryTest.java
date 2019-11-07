package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.event.EventDispatcher;
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
@PrepareForTest(Bukkit.class)
public class EquipmentFactoryTest {

    private ConfigurationSection section;
    private EventDispatcher eventDispatcher;
    private IgnitionSystemFactory ignitionSystemFactory;
    private InternalsProvider internals;
    private ItemConfig equipmentConfig;
    private String id;
    private TacticalEffectFactory tacticalEffectFactory;
    private TaskRunner taskRunner;
    private Translator translator;

    @Before
    public void setUp() {
        this.section = mock(ConfigurationSection.class);
        this.eventDispatcher = mock(EventDispatcher.class);
        this.equipmentConfig = mock(ItemConfig.class);
        this.internals = mock(InternalsProvider.class);
        this.taskRunner = mock(TaskRunner.class);
        this.translator = mock(Translator.class);

        this.id = "Id";
        this.ignitionSystemFactory = new IgnitionSystemFactory(taskRunner);
        this.tacticalEffectFactory = new TacticalEffectFactory(taskRunner);

        PowerMockito.mockStatic(Bukkit.class);

        ItemFactory itemFactory = mock(ItemFactory.class);
        ItemMeta itemMeta = mock(ItemMeta.class, withSettings().extraInterfaces(Damageable.class)); // Add the Damageable interface so the ItemMeta can be casted when setting the durability

        when(Bukkit.getItemFactory()).thenReturn(itemFactory);
        when(itemFactory.getItemMeta(any())).thenReturn(itemMeta);
        when(equipmentConfig.getItemConfigurationSection(id)).thenReturn(section);
        when(section.getString("Material")).thenReturn("AIR,1");
    }

    @Test(expected = FactoryCreationException.class)
    public void makeEquipmentInvalidType() {
        when(section.getString("EquipmentType")).thenReturn("INVALID");

        EquipmentFactory factory = new EquipmentFactory(equipmentConfig, eventDispatcher, ignitionSystemFactory, internals, tacticalEffectFactory, taskRunner, translator);
        factory.make(id);
    }

    @Test(expected = FactoryCreationException.class)
    public void makeLethalWithFailingValidation() {
        when(section.getInt("Amount")).thenReturn(-1);
        when(section.getString("EquipmentType")).thenReturn("LETHAL");

        EquipmentFactory factory = new EquipmentFactory(equipmentConfig, eventDispatcher, ignitionSystemFactory, internals, tacticalEffectFactory, taskRunner, translator);
        factory.make(id);
    }

    @Test(expected = FactoryCreationException.class)
    public void makeTacticalWithFailingValidation() {
        when(section.getInt("Amount")).thenReturn(-1);
        when(section.getString("Effect")).thenReturn("BLINDNESS");
        when(section.getString("EquipmentType")).thenReturn("TACTICAL");

        EquipmentFactory factory = new EquipmentFactory(equipmentConfig, eventDispatcher, ignitionSystemFactory, internals, tacticalEffectFactory, taskRunner, translator);
        factory.make(id);
    }

    @Test
    public void makeLethal() {
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

        EquipmentFactory factory = new EquipmentFactory(equipmentConfig, eventDispatcher, ignitionSystemFactory, internals, tacticalEffectFactory, taskRunner, translator);
        Equipment equipment = factory.make(id);

        assertNotNull(equipment);
        assertEquals(id, equipment.getMetadata().getId());
        assertTrue(equipment instanceof Lethal);
    }

    @Test
    public void makeTactical() {
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

        EquipmentFactory factory = new EquipmentFactory(equipmentConfig, eventDispatcher, ignitionSystemFactory, internals, tacticalEffectFactory, taskRunner, translator);
        Equipment equipment = factory.make(id);

        assertNotNull(equipment);
        assertEquals(id, equipment.getMetadata().getId());
        assertTrue(equipment instanceof Tactical);
    }
}

package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.Translator;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.storage.BattlegroundsConfig;
import com.matsg.battlegrounds.api.storage.ItemConfig;
import com.matsg.battlegrounds.api.item.Firearm;
import com.matsg.battlegrounds.api.item.Gun;
import com.matsg.battlegrounds.api.item.Launcher;
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

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        BattlegroundsConfig.class,
        BattlegroundsPlugin.class,
        Bukkit.class,
        Translator.class
})
public class FirearmFactoryTest {

    private Battlegrounds plugin;
    private BattlegroundsConfig config;
    private ConfigurationSection section;
    private ItemConfig firearmConfig;
    private String id;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.config = PowerMockito.mock(BattlegroundsConfig.class);
        this.section = mock(ConfigurationSection.class);
        this.firearmConfig = mock(ItemConfig.class);

        this.id = "Id";

        PowerMockito.mockStatic(BattlegroundsPlugin.class);
        PowerMockito.mockStatic(Bukkit.class);
        PowerMockito.mockStatic(Translator.class);

        config.pierceableBlocks = Collections.emptyList();

        ItemFactory itemFactory = mock(ItemFactory.class);
        ItemMeta itemMeta = mock(ItemMeta.class, withSettings().extraInterfaces(Damageable.class)); // Add the Damageable interface so the ItemMeta can be casted when setting the durability

        when(BattlegroundsPlugin.getPlugin()).thenReturn(plugin);
        when(Bukkit.getItemFactory()).thenReturn(itemFactory);
        when(itemFactory.getItemMeta(any())).thenReturn(itemMeta);
        when(firearmConfig.getItemConfigurationSection(id)).thenReturn(section);
        when(plugin.getBattlegroundsConfig()).thenReturn(config);
        when(section.getString("Material")).thenReturn("AIR,1");
        when(section.getString("Projectile.Material")).thenReturn("AIR,1");
        when(Translator.translate(anyString())).thenReturn("test");
    }

    @Test(expected = FactoryCreationException.class)
    public void testMakeFirearmInvalidType() {
        when(section.getString("FirearmType")).thenReturn("INVALID");

        FirearmFactory factory = new FirearmFactory(firearmConfig);
        factory.make(id);
    }

    @Test(expected = FactoryCreationException.class)
    public void testMakeGunWithFailingValidation() {
        when(section.getInt("Ammo.Magazine")).thenReturn(-1);
        when(section.getString("FirearmType")).thenReturn("ASSAULT_RIFLE");

        FirearmFactory factory = new FirearmFactory(firearmConfig);
        factory.make(id);
    }

    @Test(expected = FactoryCreationException.class)
    public void testMakeLauncherWithFailingValidation() {
        when(section.getInt("Ammo.Magazine")).thenReturn(-1);
        when(section.getString("FirearmType")).thenReturn("LAUNCHER");

        FirearmFactory factory = new FirearmFactory(firearmConfig);
        factory.make(id);
    }

    @Test
    public void testMakeGun() {
        when(section.getDouble("Accuracy")).thenReturn(0.5);
        when(section.getInt("Ammo.Magazine")).thenReturn(1);
        when(section.getInt("Ammo.Max")).thenReturn(1);
        when(section.getInt("Ammo.Supply")).thenReturn(1);
        when(section.getString("Attachments")).thenReturn("EXTENDED_CLIP(30), SUPPRESSOR"); // Cover both cases where attachments have and don't have parameters
        when(section.getString("FirearmType")).thenReturn("ASSAULT_RIFLE");
        when(section.getInt("FireMode.Burst")).thenReturn(1);
        when(section.getInt("FireMode.Cooldown")).thenReturn(1);
        when(section.getInt("FireMode.FireRate")).thenReturn(1);
        when(section.getString("FireMode.Type")).thenReturn("AUTOMATIC");
        when(section.getDouble("HeadshotMultiplier")).thenReturn(1.0);
        when(section.getDouble("Range.Long.Damage")).thenReturn(1.0);
        when(section.getDouble("Range.Long.Distance")).thenReturn(1.0);
        when(section.getDouble("Range.Medium.Damage")).thenReturn(1.0);
        when(section.getDouble("Range.Medium.Distance")).thenReturn(1.0);
        when(section.getDouble("Range.Short.Damage")).thenReturn(1.0);
        when(section.getDouble("Range.Short.Distance")).thenReturn(1.0);
        when(section.getInt("Reload.Duration")).thenReturn(1);
        when(section.getString("Reload.Type")).thenReturn("MAGAZINE");
        when(section.getName()).thenReturn(id);

        FirearmFactory factory = new FirearmFactory(firearmConfig);
        Firearm firearm = factory.make(id);

        assertNotNull(firearm);
        assertEquals(id, firearm.getId());
        assertTrue(firearm instanceof Gun);
    }

    @Test
    public void testMakeLauncher() {
        when(section.getDouble("Accuracy")).thenReturn(0.5);
        when(section.getInt("Ammo.Magazine")).thenReturn(1);
        when(section.getInt("Ammo.Max")).thenReturn(1);
        when(section.getInt("Ammo.Supply")).thenReturn(1);
        when(section.getString("FirearmType")).thenReturn("LAUNCHER");
        when(section.getInt("FireMode.Cooldown")).thenReturn(1);
        when(section.getString("FireMode.LaunchType")).thenReturn("GRENADE");
        when(section.getDouble("Projectile.Speed")).thenReturn(1.0);
        when(section.getDouble("Range.Long.Damage")).thenReturn(1.0);
        when(section.getDouble("Range.Long.Distance")).thenReturn(1.0);
        when(section.getDouble("Range.Medium.Damage")).thenReturn(1.0);
        when(section.getDouble("Range.Medium.Distance")).thenReturn(1.0);
        when(section.getDouble("Range.Short.Damage")).thenReturn(1.0);
        when(section.getDouble("Range.Short.Distance")).thenReturn(1.0);
        when(section.getInt("Reload.Duration")).thenReturn(1);
        when(section.getString("Reload.Type")).thenReturn("MAGAZINE");
        when(section.getName()).thenReturn(id);

        FirearmFactory factory = new FirearmFactory(firearmConfig);
        Firearm firearm = factory.make(id);

        assertNotNull(firearm);
        assertEquals(id, firearm.getId());
        assertTrue(firearm instanceof Launcher);
    }
}

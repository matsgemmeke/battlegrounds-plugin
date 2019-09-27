package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.api.event.EventDispatcher;
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
@PrepareForTest(Bukkit.class)
public class FirearmFactoryTest {

    private BattlegroundsConfig config;
    private ConfigurationSection section;
    private EventDispatcher eventDispatcher;
    private FireModeFactory fireModeFactory;
    private ItemConfig firearmConfig;
    private LaunchSystemFactory launchSystemFactory;
    private ReloadSystemFactory reloadSystemFactory;
    private String id;
    private TaskRunner taskRunner;
    private Translator translator;
    private Version version;

    @Before
    public void setUp() {
        this.config = mock(BattlegroundsConfig.class);
        this.section = mock(ConfigurationSection.class);
        this.eventDispatcher = mock(EventDispatcher.class);
        this.firearmConfig = mock(ItemConfig.class);
        this.taskRunner = mock(TaskRunner.class);
        this.translator = mock(Translator.class);
        this.version = mock(Version.class);

        this.fireModeFactory = new FireModeFactory(taskRunner);
        this.id = "Id";
        this.launchSystemFactory = new LaunchSystemFactory(taskRunner, version);
        this.reloadSystemFactory = new ReloadSystemFactory();

        PowerMockito.mockStatic(Bukkit.class);

        config.pierceableMaterials = Collections.emptyList();

        ItemFactory itemFactory = mock(ItemFactory.class);
        ItemMeta itemMeta = mock(ItemMeta.class, withSettings().extraInterfaces(Damageable.class)); // Add the Damageable interface so the ItemMeta can be casted when setting the durability
        Translator translator = mock(Translator.class);

        when(Bukkit.getItemFactory()).thenReturn(itemFactory);
        when(itemFactory.getItemMeta(any())).thenReturn(itemMeta);
        when(firearmConfig.getItemConfigurationSection(id)).thenReturn(section);
        when(section.getString("Material")).thenReturn("AIR,1");
        when(section.getString("Projectile.Material")).thenReturn("AIR,1");
    }

    @Test(expected = FactoryCreationException.class)
    public void makeFirearmInvalidType() {
        when(section.getString("FirearmType")).thenReturn("INVALID");

        FirearmFactory factory = new FirearmFactory(
                firearmConfig,
                eventDispatcher,
                fireModeFactory,
                launchSystemFactory,
                reloadSystemFactory,
                taskRunner,
                translator,
                version,
                config
        );
        factory.make(id);
    }

    @Test(expected = FactoryCreationException.class)
    public void makeGunWithFailingValidation() {
        when(section.getInt("Ammo.Magazine")).thenReturn(-1);
        when(section.getString("FirearmType")).thenReturn("ASSAULT_RIFLE");

        FirearmFactory factory = new FirearmFactory(
                firearmConfig,
                eventDispatcher,
                fireModeFactory,
                launchSystemFactory,
                reloadSystemFactory,
                taskRunner,
                translator,
                version,
                config
        );
        factory.make(id);
    }

    @Test(expected = FactoryCreationException.class)
    public void makeLauncherWithFailingValidation() {
        when(section.getInt("Ammo.Magazine")).thenReturn(-1);
        when(section.getString("FirearmType")).thenReturn("LAUNCHER");

        FirearmFactory factory = new FirearmFactory(
                firearmConfig,
                eventDispatcher,
                fireModeFactory,
                launchSystemFactory,
                reloadSystemFactory,
                taskRunner,
                translator,
                version,
                config
        );
        factory.make(id);
    }

    @Test
    public void makeGun() {
        when(section.getDouble("Accuracy")).thenReturn(0.5);
        when(section.getInt("Ammo.Magazine")).thenReturn(1);
        when(section.getInt("Ammo.Max")).thenReturn(1);
        when(section.getInt("Ammo.Supply")).thenReturn(1);
        when(section.getString("Attachments")).thenReturn("EXTENDED_CLIP(30), SUPPRESSOR"); // Cover both cases where attachments have and don't have parameters
        when(section.getString("FirearmType")).thenReturn("ASSAULT_RIFLE");
        when(section.getInt("FireMode.Burst")).thenReturn(1);
        when(section.getInt("FireMode.Cooldown")).thenReturn(1);
        when(section.getInt("FireMode.FireRate")).thenReturn(1);
        when(section.getString("FireMode.Type")).thenReturn("FULLY_AUTOMATIC");
        when(section.getDouble("HeadshotMultiplier")).thenReturn(1.0);
        when(section.getDouble("Range.Long.Damage")).thenReturn(1.0);
        when(section.getDouble("Range.Long.Distance")).thenReturn(1.0);
        when(section.getDouble("Range.Medium.Damage")).thenReturn(1.0);
        when(section.getDouble("Range.Medium.Distance")).thenReturn(1.0);
        when(section.getDouble("Range.Short.Damage")).thenReturn(1.0);
        when(section.getDouble("Range.Short.Distance")).thenReturn(1.0);
        when(section.getInt("Reload.Duration")).thenReturn(1);
        when(section.getString("Reload.System")).thenReturn("MAGAZINE");
        when(section.getName()).thenReturn(id);

        FirearmFactory factory = new FirearmFactory(
                firearmConfig,
                eventDispatcher,
                fireModeFactory,
                launchSystemFactory,
                reloadSystemFactory,
                taskRunner,
                translator,
                version,
                config
        );
        Firearm firearm = factory.make(id);

        assertNotNull(firearm);
        assertEquals(id, firearm.getMetadata().getId());
        assertTrue(firearm instanceof Gun);
    }

    @Test
    public void makeLauncher() {
        when(section.getDouble("Accuracy")).thenReturn(0.5);
        when(section.getInt("Ammo.Magazine")).thenReturn(1);
        when(section.getInt("Ammo.Max")).thenReturn(1);
        when(section.getInt("Ammo.Supply")).thenReturn(1);
        when(section.getString("FirearmType")).thenReturn("LAUNCHER");
        when(section.getInt("FireMode.Cooldown")).thenReturn(1);
        when(section.getString("FireMode.LaunchSystem")).thenReturn("GRENADE");
        when(section.getDouble("Projectile.Speed")).thenReturn(1.0);
        when(section.getDouble("Range.Long.Damage")).thenReturn(1.0);
        when(section.getDouble("Range.Long.Distance")).thenReturn(1.0);
        when(section.getDouble("Range.Medium.Damage")).thenReturn(1.0);
        when(section.getDouble("Range.Medium.Distance")).thenReturn(1.0);
        when(section.getDouble("Range.Short.Damage")).thenReturn(1.0);
        when(section.getDouble("Range.Short.Distance")).thenReturn(1.0);
        when(section.getInt("Reload.Duration")).thenReturn(1);
        when(section.getString("Reload.System")).thenReturn("MAGAZINE");
        when(section.getName()).thenReturn(id);

        FirearmFactory factory = new FirearmFactory(
                firearmConfig,
                eventDispatcher,
                fireModeFactory,
                launchSystemFactory,
                reloadSystemFactory,
                taskRunner,
                translator,
                version,
                config
        );
        Firearm firearm = factory.make(id);

        assertNotNull(firearm);
        assertEquals(id, firearm.getMetadata().getId());
        assertTrue(firearm instanceof Launcher);
    }
}

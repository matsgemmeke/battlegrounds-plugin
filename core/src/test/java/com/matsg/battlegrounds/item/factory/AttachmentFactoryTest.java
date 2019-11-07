package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.storage.ItemConfig;
import com.matsg.battlegrounds.api.item.Attachment;
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

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Bukkit.class)
public class AttachmentFactoryTest {

    private ConfigurationSection modifiers, section;
    private FireModeFactory fireModeFactory;
    private ItemConfig attachmentConfig;
    private ReloadSystemFactory reloadSystemFactory;
    private Set<String> modifierSet;
    private String id;
    private TaskRunner taskRunner;

    @Before
    public void setUp() {
        this.modifiers = mock(ConfigurationSection.class);
        this.section = mock(ConfigurationSection.class);
        this.attachmentConfig = mock(ItemConfig.class);
        this.taskRunner = mock(TaskRunner.class);

        this.fireModeFactory = new FireModeFactory(taskRunner);
        this.id = "Id";
        this.modifierSet = new HashSet<>();
        this.reloadSystemFactory = new ReloadSystemFactory();

        PowerMockito.mockStatic(Bukkit.class);

        ItemFactory itemFactory = mock(ItemFactory.class);
        ItemMeta itemMeta = mock(ItemMeta.class, withSettings().extraInterfaces(Damageable.class)); // Add the Damageable interface so the ItemMeta can be casted when setting the durability

        when(Bukkit.getItemFactory()).thenReturn(itemFactory);
        when(itemFactory.getItemMeta(any())).thenReturn(itemMeta);
        when(attachmentConfig.getItemConfigurationSection(id)).thenReturn(section);
        when(modifiers.getKeys(false)).thenReturn(modifierSet);
        when(section.getConfigurationSection("Modifiers")).thenReturn(modifiers);
        when(section.getString("Material")).thenReturn("AIR,1");
    }

    @Test
    public void makeAttachmentWithoutModifiers() {
        when(section.getName()).thenReturn(id);
        when(section.getString("GunPart")).thenReturn("MAGAZINE");

        AttachmentFactory factory = new AttachmentFactory(attachmentConfig, fireModeFactory, reloadSystemFactory);
        Attachment attachment = factory.make(id);

        assertNotNull(attachment);
        assertEquals(id, attachment.getMetadata().getId());
    }

    @Test
    public void makeAttachmentWithModifiers() {
        String attributeId = "ammo-magazine-size";

        modifierSet.add(attributeId);

        when(modifiers.getString(attributeId + ".regex")).thenReturn("=1");
        when(modifiers.getString(attributeId + ".type")).thenReturn("integer");
        when(section.getName()).thenReturn(id);
        when(section.getString("GunPart")).thenReturn("MAGAZINE");

        AttachmentFactory factory = new AttachmentFactory(attachmentConfig, fireModeFactory, reloadSystemFactory);
        Attachment attachment = factory.make(id);

        assertNotNull(attachment);
        assertEquals(id, attachment.getMetadata().getId());
    }

    @Test(expected = FactoryCreationException.class)
    public void makeAttachmantInvalidGunPart() {
        when(section.getString("GunPart")).thenReturn("INVALID");

        AttachmentFactory factory = new AttachmentFactory(attachmentConfig, fireModeFactory, reloadSystemFactory);
        factory.make(id);
    }
}

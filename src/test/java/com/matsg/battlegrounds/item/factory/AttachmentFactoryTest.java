package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
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
@PrepareForTest({
        BattlegroundsPlugin.class,
        Bukkit.class
})
public class AttachmentFactoryTest {

    private Battlegrounds plugin;
    private ConfigurationSection modifiers, section;
    private ItemConfig attachmentConfig;
    private Set<String> modifierSet;
    private String id;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.modifiers = mock(ConfigurationSection.class);
        this.section = mock(ConfigurationSection.class);
        this.attachmentConfig = mock(ItemConfig.class);

        this.id = "Id";
        this.modifierSet = new HashSet<>();

        PowerMockito.mockStatic(BattlegroundsPlugin.class);
        PowerMockito.mockStatic(Bukkit.class);

        ItemFactory itemFactory = mock(ItemFactory.class);
        ItemMeta itemMeta = mock(ItemMeta.class, withSettings().extraInterfaces(Damageable.class)); // Add the Damageable interface so the ItemMeta can be casted when setting the durability
        Translator translator = mock(Translator.class);

        when(BattlegroundsPlugin.getPlugin()).thenReturn(plugin);
        when(Bukkit.getItemFactory()).thenReturn(itemFactory);
        when(itemFactory.getItemMeta(any())).thenReturn(itemMeta);
        when(attachmentConfig.getItemConfigurationSection(id)).thenReturn(section);
        when(modifiers.getKeys(false)).thenReturn(modifierSet);
        when(plugin.getTranslator()).thenReturn(translator);
        when(section.getConfigurationSection("Modifiers")).thenReturn(modifiers);
        when(section.getString("Material")).thenReturn("AIR,1");
    }

    @Test
    public void testMakeAttachmentWithoutModifiers() {
        when(section.getName()).thenReturn(id);
        when(section.getString("GunPart")).thenReturn("MAGAZINE");

        AttachmentFactory factory = new AttachmentFactory(plugin, attachmentConfig);
        Attachment attachment = factory.make(id);

        assertNotNull(attachment);
        assertEquals(id, attachment.getId());
    }

    @Test
    public void testMakeAttachmentWithModifiers() {
        String attributeId = "ammo-magazine-size";

        modifierSet.add(attributeId);

        when(modifiers.getString(attributeId + ".regex")).thenReturn("=1");
        when(modifiers.getString(attributeId + ".type")).thenReturn("integer");
        when(section.getName()).thenReturn(id);
        when(section.getString("GunPart")).thenReturn("MAGAZINE");

        AttachmentFactory factory = new AttachmentFactory(plugin, attachmentConfig);
        Attachment attachment = factory.make(id);

        assertNotNull(attachment);
        assertEquals(id, attachment.getId());
    }

    @Test(expected = FactoryCreationException.class)
    public void testMakeAttachmantInvalidGunPart() {
        when(section.getString("GunPart")).thenReturn("INVALID");

        AttachmentFactory factory = new AttachmentFactory(plugin, attachmentConfig);
        factory.make(id);
    }
}

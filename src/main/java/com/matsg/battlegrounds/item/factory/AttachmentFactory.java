package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.api.config.ItemConfig;
import com.matsg.battlegrounds.api.item.Attachment;
import com.matsg.battlegrounds.api.item.AttributeModifier;
import com.matsg.battlegrounds.api.item.ItemFactory;
import com.matsg.battlegrounds.item.BattleAttachment;
import com.matsg.battlegrounds.item.BattleGunPart;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.item.attributes.RegexAttributeModifier;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class AttachmentFactory implements ItemFactory<Attachment> {

    private ItemConfig attachmentConfig;

    public AttachmentFactory(ItemConfig attachmentConfig) {
        this.attachmentConfig = attachmentConfig;
    }

    public Attachment make(String id) {
        ConfigurationSection section = attachmentConfig.getItemConfigurationSection(id);
        String[] material = section.getString("Material").split(",");

        try {
            Attachment attachment = new BattleAttachment(
                    id,
                    section.getString("DisplayName"),
                    section.getString("Description"),
                    new ItemStackBuilder(Material.valueOf(material[0])).build(),
                    AttributeValidator.shouldEqualOrBeHigherThan(Short.valueOf(material[1]), (short) 0),
                    BattleGunPart.valueOf(section.getString("GunPart")),
                    getModifiers(section),
                    section.getBoolean("Toggleable")
            );
            attachment.update();
            return attachment;
        } catch (Exception e) {
            throw new FactoryCreationException(e.getMessage(), e);
        }
    }

    private Map<String, AttributeModifier> getModifiers(ConfigurationSection section) {
        ConfigurationSection modifiersSection = section.getConfigurationSection("Modifiers");
        Map<String, AttributeModifier> map = new HashMap<>();

        for (String attributeId : modifiersSection.getKeys(false)) {
            String regex = modifiersSection.getString(attributeId);
            map.put(attributeId, new RegexAttributeModifier(regex));
        }

        return map;
    }
}

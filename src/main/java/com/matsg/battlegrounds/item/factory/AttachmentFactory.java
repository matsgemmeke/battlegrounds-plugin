package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.api.storage.ItemConfig;
import com.matsg.battlegrounds.api.item.Attachment;
import com.matsg.battlegrounds.api.util.AttributeModifier;
import com.matsg.battlegrounds.api.item.ItemFactory;
import com.matsg.battlegrounds.item.BattleAttachment;
import com.matsg.battlegrounds.item.BattleGunPart;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.item.modifier.*;
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
                    new ItemStackBuilder(Material.valueOf(material[0])).setDurability(Short.valueOf(material[1])).build(),
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

    private AttributeModifier getModifier(String regex, String type) {
        if (type.equals("boolean")) {
            return new BooleanAttributeModifier(regex);
        }
        if (type.equals("firemode")) {
            return new FireModeAttributeModifier(regex);
        }
        if (type.equals("float")) {
            return new FloatAttributeModifier(regex);
        }
        if (type.equals("integer")) {
            return new IntegerAttributeModifier(regex);
        }
        if (type.equals("reloadtype")) {
            return new ReloadTypeAttributeModifier(regex);
        }
        return null;
    }

    private Map<String, AttributeModifier> getModifiers(ConfigurationSection section) {
        ConfigurationSection modifiersSection = section.getConfigurationSection("Modifiers");
        Map<String, AttributeModifier> map = new HashMap<>();

        for (String attributeId : modifiersSection.getKeys(false)) {
            String regex = modifiersSection.getString(attributeId + ".regex");
            String type = modifiersSection.getString(attributeId + ".type");
            AttributeModifier modifier = getModifier(regex, type);

            map.put(attributeId, modifier);
        }

        return map;
    }
}

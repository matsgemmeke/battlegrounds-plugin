package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.api.config.ItemConfig;
import com.matsg.battlegrounds.api.item.Attachment;
import com.matsg.battlegrounds.api.item.AttributeModifier;
import com.matsg.battlegrounds.api.item.AttributeValue;
import com.matsg.battlegrounds.api.item.ItemFactory;
import com.matsg.battlegrounds.item.BattleAttachment;
import com.matsg.battlegrounds.item.BattleGunPart;
import com.matsg.battlegrounds.item.ItemStackBuilder;
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
        Map<String, AttributeModifier> map = new HashMap<>();
        for (String attributeId : section.getConfigurationSection("Modifiers").getKeys(false)) {
            String ex = section.getString("Modifiers." + attributeId), exValue = ex.substring(1, ex.length());
            AttachmentOperator operator = AttachmentOperator.fromText(ex.substring(0, 1));

            map.put(attributeId, new AttributeModifier() {
                public AttributeValue modify(AttributeValue attributeValue, Object... args) {
                    if (exValue.startsWith("arg")) {
                        int index = Integer.parseInt(exValue.substring(3, exValue.length())) - 1;
                        if (args != null && args.length >= index) {
                            attributeValue.setValue(operator.apply(attributeValue.getValue(), attributeValue.parseString(args[index].toString())));
                        }
                    } else {
                        attributeValue.setValue(operator.apply(attributeValue.getValue(), attributeValue.parseString(exValue)));
                    }
                    return attributeValue;
                }
            });
        }
        return map;
    }
}

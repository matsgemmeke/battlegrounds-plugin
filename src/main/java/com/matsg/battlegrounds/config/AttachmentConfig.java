package com.matsg.battlegrounds.config;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.AbstractYaml;
import com.matsg.battlegrounds.api.config.ItemConfig;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.item.BattleAttachment;
import com.matsg.battlegrounds.item.BattleGunPart;
import com.matsg.battlegrounds.util.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttachmentConfig extends AbstractYaml implements ItemConfig<Attachment> {

    private ItemSerializer serializer;
    private Map<String, Attachment> attachments;

    public AttachmentConfig(Battlegrounds plugin) throws IOException {
        super(plugin, plugin.getDataFolder().getPath() + "/items", "attachments.yml", false);
        this.serializer = prepareSerializer();
        setup();
    }

    public Attachment get(String arg) {
        for (Attachment attachment : attachments.values()) {
            if (attachment.getId().equals(arg) || attachment.getName().equals(arg)) {
                return attachment.clone();
            }
        }
        return null;
    }

    public List<Attachment> getList() {
        List<Attachment> list = new ArrayList<>();
        for (Attachment attachment : attachments.values()) {
            list.add(attachment.clone()); // Create a deep copy of the list
        }
        return list;
    }

    public List<Attachment> getList(ItemType itemType) {
        return getList(); // No subtypes for attachments
    }

    private Map<String, AttributeModifier> getModifiers(String id) {
        Map<String, AttributeModifier> map = new HashMap<>();
        for (String attributeId : getConfigurationSection(id + ".Modifiers").getKeys(false)) {
            String ex = getString(id + ".Modifiers." + attributeId), exValue = ex.substring(1, ex.length());
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

    private ItemSerializer prepareSerializer() {
        return new ItemSerializer<Attachment>() {
            Attachment getFromSection(ConfigurationSection section) throws ItemFormatException {
                String id = section.getName();
                String[] material = section.getString("Material").split(",");
                try {
                    return new BattleAttachment(
                            id,
                            section.getString("DisplayName"),
                            section.getString("Description"),
                            new ItemStackBuilder(Material.valueOf(material[0])).setDurability(Short.parseShort(material[1])).build(),
                            BattleGunPart.valueOf(section.getString("GunPart")),
                            getModifiers(id),
                            section.getBoolean("Toggleable")
                    );
                } catch (Exception e) {
                    throw new ItemFormatException("Invalid item format " + section.getName() + ": " + e.getMessage());
                }
            }
        };
    }

    private Attachment readAttachmentConfiguration(ConfigurationSection section) throws ItemFormatException {
        return (Attachment) serializer.getFromSection(section);
    }

    private void setup() {
        attachments = new HashMap<>();

        for (String id : getConfigurationSection("").getKeys(false)) {
            Attachment attachment;

            try {
                attachment = readAttachmentConfiguration(getConfigurationSection(id));
            } catch (Exception e) {
                plugin.getLogger().severe(e.getMessage());
                continue;
            }

            attachments.put(attachment.getName(), attachment);
        }
    }
}
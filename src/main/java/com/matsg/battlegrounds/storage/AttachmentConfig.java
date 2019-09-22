package com.matsg.battlegrounds.storage;

import com.matsg.battlegrounds.api.storage.AbstractYaml;
import com.matsg.battlegrounds.api.storage.ItemConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AttachmentConfig extends AbstractYaml implements ItemConfig {

    public AttachmentConfig(String filePath, InputStream resource) throws IOException {
        super("attachments.yml", filePath + "/items", resource, false);
    }

    public ConfigurationSection getItemConfigurationSection(String id) {
        return getConfigurationSection(id);
    }

    public List<String> getItemList() {
        List<String> list = new ArrayList<>();
        list.addAll(getConfigurationSection("").getKeys(false));
        return list;
    }

    public List<String> getItemList(String itemType) {
        // Attachments have no subtypes
        return getItemList();
    }
}

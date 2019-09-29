package com.matsg.battlegrounds.storage;

import com.matsg.battlegrounds.api.storage.AbstractYaml;
import com.matsg.battlegrounds.api.storage.ItemConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EquipmentConfig extends AbstractYaml implements ItemConfig {

    public EquipmentConfig(String filePath, InputStream resource) throws IOException {
        super("equipment.yml", filePath, resource, false);
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
        List<String> list = new ArrayList<>();
        for (String id : getItemList()) {
            if (getItemConfigurationSection(id).getString("EquipmentType").equals(itemType)) {
                list.add(id);
            }
        }
        return list;
    }
}

package com.matsg.battlegrounds.config;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.AbstractYaml;
import com.matsg.battlegrounds.api.config.ItemConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MeleeWeaponConfig extends AbstractYaml implements ItemConfig {

    public MeleeWeaponConfig(Battlegrounds plugin) throws IOException {
        super(plugin, plugin.getDataFolder().getPath() + "/items", "melee_weapons.yml", false);
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
        // Knives have no subtypes
        return getItemList();
    }
}

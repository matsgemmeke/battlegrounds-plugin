package com.matsg.battlegrounds.config;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.LoadoutClass;
import com.matsg.battlegrounds.item.BattleLoadoutClass;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefaultClasses extends AbstractYaml {

    public DefaultClasses(Battlegrounds plugin) throws IOException {
        super(plugin, plugin.getDataFolder().getPath() + "/classes", "default-classes.yml", true);
    }

    public List<LoadoutClass> getList() {
        List<LoadoutClass> list = new ArrayList<>();
        for (String loadoutClass : getConfigurationSection("default_classes").getKeys(false)) {
            list.add(parseLoadoutClass(getConfigurationSection("default_classes." + loadoutClass)));
        }
        return list;
    }

    private LoadoutClass parseLoadoutClass(ConfigurationSection section) {
        return new BattleLoadoutClass(
                section.getString("Name"),
                plugin.getFireArmConfig().get(section.getString("Primary")),
                plugin.getFireArmConfig().get(section.getString("Secondary")),
                plugin.getEquipmentConfig().get(section.getString("Equipment")),
                plugin.getKnifeConfig().get(section.getString("Knife"))
        );
    }
}
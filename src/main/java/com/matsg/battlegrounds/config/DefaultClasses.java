package com.matsg.battlegrounds.config;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.AbstractYaml;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.item.BattleLoadout;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefaultClasses extends AbstractYaml {

    public DefaultClasses(Battlegrounds plugin) throws IOException {
        super(plugin, "default-classes.yml", true);
    }

    public List<Loadout> getList() {
        List<Loadout> list = new ArrayList<>();
        for (String loadoutId : getKeys(false)) {
            list.add(parseLoadout(Integer.parseInt(loadoutId), getConfigurationSection(loadoutId)));
        }
        return list;
    }

    private Loadout parseLoadout(int loadoutId, ConfigurationSection section) {
        return new BattleLoadout(
                loadoutId,
                section.getString("Name"),
                plugin.getFireArmConfig().get(section.getString("Primary")),
                plugin.getFireArmConfig().get(section.getString("Secondary")),
                plugin.getEquipmentConfig().get(section.getString("Equipment")),
                plugin.getKnifeConfig().get(section.getString("Knife"))
        );
    }
}
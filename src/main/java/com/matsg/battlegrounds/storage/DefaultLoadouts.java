package com.matsg.battlegrounds.storage;

import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.storage.AbstractYaml;
import com.matsg.battlegrounds.item.BattleLoadout;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DefaultLoadouts extends AbstractYaml {

    private ItemFactory<Equipment> equipmentFactory;
    private ItemFactory<Firearm> firearmFactory;
    private ItemFactory<MeleeWeapon> meleeWeaponFactory;

    public DefaultLoadouts(
            String filePath,
            InputStream resource,
            ItemFactory<Firearm> firearmFactory,
            ItemFactory<Equipment> equipmentFactory,
            ItemFactory<MeleeWeapon> meleeWeaponFactory
    ) throws IOException {
        super("default_loadouts.yml", filePath, resource, true);
        this.firearmFactory = firearmFactory;
        this.equipmentFactory = equipmentFactory;
        this.meleeWeaponFactory = meleeWeaponFactory;
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
                firearmFactory.make(section.getString("Primary.Id")),
                firearmFactory.make(section.getString("Secondary.Id")),
                equipmentFactory.make(section.getString("Equipment")),
                meleeWeaponFactory.make(section.getString("MeleeWeapon"))
        );
    }
}

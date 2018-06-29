package com.matsg.battlegrounds.config;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.AbstractYaml;
import com.matsg.battlegrounds.api.config.WeaponConfig;
import com.matsg.battlegrounds.api.item.Knife;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.item.WeaponType;
import com.matsg.battlegrounds.item.BattleKnife;
import com.matsg.battlegrounds.util.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnifeConfig extends AbstractYaml implements WeaponConfig<Knife> {

    private Map<String, Knife> knives;
    private WeaponSerializer serializer;

    public KnifeConfig(Battlegrounds plugin) throws IOException {
        super(plugin, plugin.getDataFolder().getPath() + "/items", "knives.yml", false);
        this.serializer = prepareSerializer();
        setup();
    }

    public Knife get(String name) {
        for (Knife knife : getList()) {
            if (knife.getName().equalsIgnoreCase(name)) {
                return knife.clone();
            }
        }
        return null;
    }

    public List<Knife> getList() {
        List<Knife> list = new ArrayList<>();
        list.addAll(knives.values());
        return list;
    }

    public List<Knife> getList(WeaponType weaponType) {
        return getList(); // No subtypes for knives
    }

    private WeaponSerializer prepareSerializer() {
        return new WeaponSerializer() {
            Weapon getFromSection(ConfigurationSection section) throws ItemFormatException {
                String name = section.getString("DisplayName");
                String[] material = section.getString("Material").split(",");
                try {
                    return new BattleKnife(
                            name,
                            section.getString("Description"),
                            new ItemStackBuilder(Material.valueOf(material[0])).build(),
                            (short) new AttributeValidator(Short.parseShort(material[1]), "Durability").shouldBeHigherThan(0),
                            new AttributeValidator(section.getDouble("Damage"), "Damage").shouldEqualOrBeHigherThan(0.0),
                            (int) new AttributeValidator(section.getInt("Amount"), "Amount").shouldBeHigherThan(0),
                            section.getBoolean("Throwable"),
                            (int) new AttributeValidator(section.getInt("Cooldown"), "Cooldown").shouldEqualOrBeHigherThan(0.0)
                    );
                } catch (ValidationFailedException e) {
                    throw new ItemFormatException("Invalid item format " + name + ": " + e.getMessage());
                }
            }
        };
    }

    private Knife readKnifeConfiguration(ConfigurationSection section) throws ItemFormatException {
        return (Knife) serializer.getFromSection(section);
    }

    private void setup() {
        knives = new HashMap<>();

        for (String knifeString : getConfigurationSection("").getKeys(false)) {
            Knife knife;

            try {
                knife = readKnifeConfiguration(getConfigurationSection(knifeString));
            } catch (Exception e) {
                plugin.getLogger().severe(e.getMessage());
                continue;
            }

            knives.put(knife.getName(), knife);
        }
    }
}
package com.matsg.battlegrounds.config;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.AbstractYaml;
import com.matsg.battlegrounds.api.config.ItemConfig;
import com.matsg.battlegrounds.api.item.ItemType;
import com.matsg.battlegrounds.api.item.Knife;
import com.matsg.battlegrounds.item.BattleKnife;
import com.matsg.battlegrounds.util.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnifeConfig extends AbstractYaml implements ItemConfig<Knife> {

    private ItemSerializer serializer;
    private Map<String, Knife> knives;

    public KnifeConfig(Battlegrounds plugin) throws IOException {
        super(plugin, plugin.getDataFolder().getPath() + "/items", "knives.yml", false);
        this.serializer = prepareSerializer();
        setup();
    }

    public Knife get(String arg) {
        for (Knife knife : knives.values()) {
            if (knife.getId().equals(arg) || knife.getName().equals(arg)) {
                return knife.clone();
            }
        }
        return null;
    }

    public List<Knife> getList() {
        List<Knife> list = new ArrayList<>();
        for (Knife knife : knives.values()) {
            list.add(knife.clone()); // Create a deep copy of the list
        }
        return list;
    }

    public List<Knife> getList(ItemType itemType) {
        return getList(); // No subtypes for knives
    }

    private ItemSerializer prepareSerializer() {
        return new ItemSerializer<Knife>() {
            Knife getFromSection(ConfigurationSection section) throws ItemFormatException {
                String[] material = section.getString("Material").split(",");
                try {
                    return new BattleKnife(
                            section.getName(),
                            section.getString("DisplayName"),
                            section.getString("Description"),
                            new ItemStackBuilder(Material.valueOf(material[0])).build(),
                            (short) new AttributeValidator(Short.parseShort(material[1]), "Durability").shouldEqualOrBeHigherThan(0),
                            new AttributeValidator(section.getDouble("Damage"), "Damage").shouldEqualOrBeHigherThan(0.0),
                            (int) new AttributeValidator(section.getInt("Amount"), "Amount").shouldBeHigherThan(0),
                            section.getBoolean("Throwable"),
                            (int) new AttributeValidator(section.getInt("Cooldown"), "Cooldown").shouldEqualOrBeHigherThan(0.0)
                    );
                } catch (ValidationFailedException e) {
                    throw new ItemFormatException("Invalid item format " + section.getName() + ": " + e.getMessage());
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
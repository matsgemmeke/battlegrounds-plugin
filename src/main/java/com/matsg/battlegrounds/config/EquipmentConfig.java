package com.matsg.battlegrounds.config;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.AbstractYaml;
import com.matsg.battlegrounds.api.config.WeaponConfig;
import com.matsg.battlegrounds.api.item.Equipment;
import com.matsg.battlegrounds.api.item.Lethal;
import com.matsg.battlegrounds.api.item.Tactical;
import com.matsg.battlegrounds.api.item.WeaponType;
import com.matsg.battlegrounds.item.BattleLethal;
import com.matsg.battlegrounds.item.BattleTactical;
import com.matsg.battlegrounds.item.BattleTacticalEffect;
import com.matsg.battlegrounds.item.EquipmentType;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipmentConfig extends AbstractYaml implements WeaponConfig<Equipment> {

    private List<WeaponSerializer> serializers;
    private Map<String, Equipment> equipment;

    public EquipmentConfig(Battlegrounds plugin) throws IOException {
        super(plugin, plugin.getDataFolder().getPath() + "/items", "equipment.yml", false);
        this.serializers = prepareSerializers();
        setup();
    }

    public Equipment get(String name) {
        for (Equipment equipment : getList()) {
            if (equipment.getName().equalsIgnoreCase(name)) {
                return equipment.clone();
            }
        }
        return null;
    }

    public List<Equipment> getList() {
        List<Equipment> list = new ArrayList<>();
        list.addAll(equipment.values());
        return list;
    }

    public List<Equipment> getList(WeaponType weaponType) {
        List<Equipment> list = new ArrayList<>();
        for (Equipment equipment : equipment.values()) {
            if (equipment.getType() == weaponType) {
                list.add(equipment);
            }
        }
        return list;
    }

    private List<WeaponSerializer> prepareSerializers() {
        List<WeaponSerializer> list = new ArrayList<>();
        list.add(new WeaponSerializer<Lethal>(EquipmentType.LETHAL) {
            Lethal getFromSection(ConfigurationSection section) throws ItemFormatException {
                String name = section.getString("DisplayName");
                String[] material = section.getString("Material").split(",");
                try {
                    return new BattleLethal(
                            name,
                            section.getString("Description"),
                            new ItemStackBuilder(Material.valueOf(material[0])).setDurability((short) section.getInt("Durability")).build(),
                            (short) new AttributeValidator(Short.parseShort(material[1]), "Durability").shouldEqualOrBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("Amount"), "Amount").shouldBeHigherThan(0),
                            new AttributeValidator(section.getDouble("Range.Long.Damage"), "Long damage").shouldEqualOrBeHigherThan(0.0),
                            new AttributeValidator(section.getDouble("Range.Long.Distance"), "Long range").shouldEqualOrBeHigherThan(0.0),
                            new AttributeValidator(section.getDouble("Range.Medium.Damage"), "Medium damage").shouldEqualOrBeHigherThan(0.0),
                            new AttributeValidator(section.getDouble("Range.Medium.Distance"), "Medium range").shouldEqualOrBeHigherThan(0.0),
                            new AttributeValidator(section.getDouble("Range.Short.Damage"), "Short damage").shouldEqualOrBeHigherThan(0.0),
                            new AttributeValidator(section.getDouble("Range.Short.Distance"), "Short range").shouldEqualOrBeHigherThan(0.0),
                            new AttributeValidator(section.getDouble("Velocity"), "Velocity").shouldEqualOrBeHigherThan(0.0),
                            (int) new AttributeValidator(section.getInt("IgnitionTime"), "Ignition time").shouldEqualOrBeHigherThan(0),
                            BattleSound.parseSoundArray(section.getString("Sound"))
                    );
                } catch (ValidationFailedException e) {
                    throw new ItemFormatException("Invalid item format " + name + ": " + e.getMessage());
                }
            }
        });
        list.add(new WeaponSerializer<Tactical>(EquipmentType.TACTICAL) {
            Tactical getFromSection(ConfigurationSection section) throws ItemFormatException {
                String name = section.getString("DisplayName");
                String[] material = section.getString("Material").split(",");
                try {
                    return new BattleTactical(
                            name,
                            section.getString("Description"),
                            new ItemStackBuilder(Material.valueOf(material[0])).setDurability((short) section.getInt("Durability")).build(),
                            (short) new AttributeValidator(Short.parseShort(material[1]), "Durability").shouldEqualOrBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("Amount"), "Amount").shouldBeHigherThan(0),
                            BattleTacticalEffect.valueOf(section.getString("Effect")),
                            (int) new AttributeValidator(section.getInt("Duration"), "Duration").shouldBeHigherThan(0),
                            new AttributeValidator(section.getDouble("Range.Long.Distance"), "Long range").shouldEqualOrBeHigherThan(0.0),
                            new AttributeValidator(section.getDouble("Range.Medium.Distance"), "Medium range").shouldEqualOrBeHigherThan(0.0),
                            new AttributeValidator(section.getDouble("Range.Short.Distance"), "Short range").shouldEqualOrBeHigherThan(0.0),
                            new AttributeValidator(section.getDouble("Velocity"), "Velocity").shouldEqualOrBeHigherThan(0.0),
                            (int) new AttributeValidator(section.getInt("IgnitionTime"), "Ignition time").shouldEqualOrBeHigherThan(0),
                            BattleSound.parseSoundArray(section.getString("Sound"))
                    );
                } catch (ValidationFailedException e) {
                    throw new ItemFormatException("Invalid item format " + name + ": " + e.getMessage());
                }
            }
        });
        return list;
    }

    private Equipment readEquipmentConfiguration(ConfigurationSection section) throws IllegalArgumentException, ItemFormatException {
        EquipmentType type = EquipmentType.valueOf(section.getString("EquipmentType"));
        for (WeaponSerializer serializer : serializers) {
            if (serializer.hasType(type)) {
                return (Equipment) serializer.getFromSection(section);
            }
        }
        throw new ItemFormatException("Invalid item format in " + section.getName() + "'s configuration in equipment.yml");
    }

    private void setup() {
        equipment = new HashMap<>();

        for (String explosiveString : getConfigurationSection("").getKeys(false)) {
            Equipment equipment;

            try {
                equipment = readEquipmentConfiguration(getConfigurationSection(explosiveString));
            } catch (Exception e) {
                plugin.getLogger().severe(e.getMessage());
                continue;
            }

            this.equipment.put(equipment.getName(), equipment);
        }
    }
}
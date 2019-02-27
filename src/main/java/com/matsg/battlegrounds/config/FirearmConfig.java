package com.matsg.battlegrounds.config;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.AbstractYaml;
import com.matsg.battlegrounds.api.config.ItemConfig;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.item.*;
import com.matsg.battlegrounds.util.BattleSound;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirearmConfig extends AbstractYaml implements ItemConfig<Firearm> {

    private List<ItemSerializer> serializers;
    private Map<String, Firearm> firearms;

    public FirearmConfig(Battlegrounds plugin) throws IOException {
        super(plugin, plugin.getDataFolder().getPath() + "/items", "guns.yml", false);
        this.serializers = prepareSerializers();
        setup();
    }

    public Firearm get(String arg) {
        for (Firearm firearm : firearms.values()) {
            if (firearm.getId().equals(arg) || firearm.getName().equals(arg)) {
                return firearm.clone();
            }
        }
        return null;
    }

    public List<Firearm> getList() {
        List<Firearm> list = new ArrayList<>();
        for (Firearm firearm : firearms.values()) {
            list.add(firearm.clone()); // Create a deep copy of the list
        }
        return list;
    }

    public List<Firearm> getList(ItemType itemType) {
        List<Firearm> list = new ArrayList<>();
        for (Firearm firearm : firearms.values()) {
            if (firearm.getType() == itemType) {
                list.add(firearm.clone());
            }
        }
        return list;
    }

    private Bullet getBulletProjectile(ConfigurationSection section) throws ItemFormatException {
        try {
            return new Bullet(
                    new AttributeValidator(section.getDouble("Range.Long.Damage")).shouldEqualOrBeHigherThan(0.0),
                    new AttributeValidator(section.getDouble("Range.Long.Distance")).shouldEqualOrBeHigherThan(0.0),
                    new AttributeValidator(section.getDouble("Range.Medium.Damage")).shouldEqualOrBeHigherThan(0.0),
                    new AttributeValidator(section.getDouble("Range.Medium.Distance")).shouldEqualOrBeHigherThan(0.0),
                    new AttributeValidator(section.getDouble("Range.Short.Damage")).shouldEqualOrBeHigherThan(0.0),
                    new AttributeValidator(section.getDouble("Range.Short.Distance")).shouldEqualOrBeHigherThan(0.0),
                    new AttributeValidator(section.getDouble("HeadshotMultiplier")).shouldBeBetween(0.0, 100.0));
        } catch (ValidationFailedException e) {
            throw new ItemFormatException("Invalid item format " + section.getName() + ": " + e.getMessage());
        }
    }

    private Map<String, String[]> getCompatibleAttachments(String string) {
        Map<String, String[]> map = new HashMap<>();
        for (String attachment : string.split(", ")) {
            String id = attachment.substring(0, attachment.contains("(") ? attachment.indexOf('(') : attachment.length());
            String[] args = new String[0];
            if (attachment.contains("(") && attachment.contains(")")) {
                args = attachment.substring(attachment.indexOf('(') + 1, attachment.indexOf(')')).split(",");
            }
            map.put(id, args);
        }
        return map;
    }

    private Lethal getLethalProjectile(ConfigurationSection section) throws ItemFormatException {
        String[] material = plugin.getBattlegroundsConfig().launcherMaterial.split(",");
        try {
            return new BattleLethal(
                    null,
                    null,
                    null,
                    new ItemStackBuilder(Material.valueOf(material[0])).setAmount(1).setDurability(Short.parseShort(material[1])).build(),
                    (short) 1, 1,
                    new AttributeValidator(section.getDouble("Range.Long.Damage"), "Long damage").shouldEqualOrBeHigherThan(0.0),
                    new AttributeValidator(section.getDouble("Range.Long.Distance"), "Long range").shouldEqualOrBeHigherThan(0.0),
                    new AttributeValidator(section.getDouble("Range.Medium.Damage"), "Medium damage").shouldEqualOrBeHigherThan(0.0),
                    new AttributeValidator(section.getDouble("Range.Medium.Distance"), "Medium range").shouldEqualOrBeHigherThan(0.0),
                    new AttributeValidator(section.getDouble("Range.Short.Damage"), "Short damage").shouldEqualOrBeHigherThan(0.0),
                    new AttributeValidator(section.getDouble("Range.Short.Distance"), "Short range").shouldEqualOrBeHigherThan(0.0),
                    plugin.getBattlegroundsConfig().launcherVelocity, 1, null);
        } catch (ValidationFailedException e) {
            throw new ItemFormatException("Invalid item format " + section.getName() + ": " + e.getMessage());
        }
    }

    private List<ItemSerializer> prepareSerializers() {
        List<ItemSerializer> list = new ArrayList<>();
        list.add(new ItemSerializer<Gun>(FirearmType.GUNS) {
            Gun getFromSection(ConfigurationSection section) throws ItemFormatException {
                String[] material = section.getString("Material").split(",");
                try {
                    return new BattleGun(
                            section.getName(),
                            section.getString("DisplayName"),
                            section.getString("Description"),
                            new ItemStackBuilder(Material.valueOf(material[0])).build(),
                            (short) new AttributeValidator(Short.parseShort(material[1]), "Durability").shouldEqualOrBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("Ammo.Magazine"), "Magazine").shouldBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("Ammo.Supply"), "Ammunition supply").shouldEqualOrBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("Ammo.Max"), "Max ammunition").shouldEqualOrBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("FireMode.FireRate"), "Fire rate").shouldBeBetween(0, 20),
                            (int) new AttributeValidator(section.getInt("FireMode.Burst"), "Burst rounds").shouldEqualOrBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("FireMode.Cooldown"), "Cooldown").shouldEqualOrBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("Reload.Duration"), "Reload duration").shouldBeHigherThan(0),
                            new AttributeValidator(section.getDouble("Accuracy"), "Accuracy").shouldBeBetween(0.0, 1.0),
                            getBulletProjectile(section),
                            FireMode.valueOf(section.getString("FireMode.Type")),
                            FirearmType.getValue(section.getString("GunType")),
                            ReloadType.valueOf(section.getString("Reload.Type")),
                            BattleSound.parseSoundArray(section.getString("Reload.Sound.Reload")),
                            BattleSound.parseSoundArray(section.getString("Shot.ShotSound")),
                            BattleSound.parseSoundArray(section.getString("Shot.SuppressedSound")),
                            getCompatibleAttachments(section.getString("Attachments"))
                    );
                } catch (ValidationFailedException e) {
                    throw new ItemFormatException("Invalid item format " + section.getName() + ": " + e.getMessage());
                }
            }
        });
        list.add(new ItemSerializer<Launcher>(FirearmType.LAUNCHER) {
            Launcher getFromSection(ConfigurationSection section) throws ItemFormatException {
                String[] material = section.getString("Material").split(",");
                try {
                    return new BattleLauncher(
                            section.getName(),
                            section.getString("DisplayName"),
                            section.getString("Description"),
                            new ItemStackBuilder(Material.valueOf(material[0])).build(),
                            (short) new AttributeValidator(Short.parseShort(material[1]), "Durability").shouldEqualOrBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("Ammo.Magazine"), "Magazine").shouldBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("Ammo.Supply"), "Ammunition supply").shouldBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("Ammo.Max"), "Max ammunition").shouldBeHigherThan(0),
                            (int) new AttributeValidator(section.getDouble("Speed"), "Launch speed").shouldBeHigherThan(0.0),
                            (int) new AttributeValidator(section.getInt("FireMode.Cooldown"), "Cooldown").shouldEqualOrBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("Reload.Duration"), "Reload duration").shouldBeHigherThan(0),
                            new AttributeValidator(section.getDouble("Accuracy"), "Accuracy").shouldBeBetween(0.0, 1.0),
                            getLethalProjectile(section),
                            LaunchType.valueOf(section.getString("FireMode.LaunchType")),
                            ReloadType.valueOf(section.getString("Reload.Type")),
                            BattleSound.parseSoundArray(section.getString("Reload.Sound.Reload")),
                            BattleSound.parseSoundArray(section.getString("Shot.ShotSound"))
                    );
                } catch (Exception e) {
                    throw new ItemFormatException("Invalid item format " + section.getName() + ": " + e.getMessage());
                }
            }
        });
        return list;
    }

    private Firearm readFirearmConfiguration(ConfigurationSection section) throws IllegalArgumentException, ItemFormatException {
        String typeString = section.getString("GunType");
        FirearmType type = FirearmType.valueOf(typeString);
        for (ItemSerializer serializer : serializers) {
            if (serializer.hasType(type)) {
                return (Firearm) serializer.getFromSection(section);
            }
        }
        throw new ItemFormatException("Invalid item format " + section.getName() + ": Unknown firearm type \"" + typeString + "\"");
    }

    private void setup() {
        firearms = new HashMap<>();

        for (String firearmName : getKeys(false)) {
            Firearm firearm;
            try {
                firearm = readFirearmConfiguration(getConfigurationSection(firearmName));
            } catch (Exception e) {
                plugin.getLogger().severe(e.getMessage());
                continue;
            }
            firearms.put(firearm.getName(), firearm);
        }
    }
}
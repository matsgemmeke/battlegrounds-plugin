package com.matsg.battlegrounds.config;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.WeaponConfig;
import com.matsg.battlegrounds.api.item.FireArm;
import com.matsg.battlegrounds.api.item.Lethal;
import com.matsg.battlegrounds.api.item.ReloadType;
import com.matsg.battlegrounds.api.item.WeaponType;
import com.matsg.battlegrounds.item.*;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireArmConfig extends AbstractYaml implements WeaponConfig<FireArm> {

    private List<WeaponSerializer> serializers;
    private Map<String, FireArm> fireArms;

    public FireArmConfig(Battlegrounds plugin) throws IOException {
        super(plugin, plugin.getDataFolder().getPath() + "/items", "guns.yml", false);
        this.serializers = prepareSerializers();
        setup();
    }

    public FireArm get(String name) {
        for (FireArm fireArm : getList()) {
            if (fireArm.getName().equalsIgnoreCase(name)) {
                return fireArm.clone();
            }
        }
        return null;
    }

    public List<FireArm> getList() {
        List<FireArm> list = new ArrayList<>();
        list.addAll(fireArms.values());
        return list;
    }

    public List<FireArm> getList(WeaponType weaponType) {
        List<FireArm> list = new ArrayList<>();
        for (FireArm fireArm : fireArms.values()) {
            if (fireArm.getType() == weaponType) {
                list.add(fireArm);
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

    private Lethal getLethalProjectile(ConfigurationSection section) throws ItemFormatException {
        try {
            return new BattleLethal(
                    null,
                    null,
                    new ItemStackBuilder(
                    Material.valueOf(plugin.getBattlegroundsConfig().getWeaponMaterial("equipment"))).setAmount(1).setDurability((short) 1).build(),
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

    private List<WeaponSerializer> prepareSerializers() {
        List<WeaponSerializer> list = new ArrayList<>();
        list.add(new WeaponSerializer(FireArmType.GUNS) {
            FireArm getFromSection(ConfigurationSection section) throws ItemFormatException {
                String name = section.getString("DisplayName");
                try {
                    return new BattleGun(
                            name,
                            getString("Description"),
                            new ItemStackBuilder(Material.valueOf(plugin.getBattlegroundsConfig().getWeaponMaterial("firearm"))).build(),
                            (short) new AttributeValidator(section.getInt("Durability"), "Durability").shouldBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("Ammo.Magazine"), "Magazine").shouldBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("Ammo.Start"), "Start ammunition").shouldEqualOrBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("Ammo.Max"), "Max ammunition").shouldEqualOrBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("FireMode.FireRate"), "Fire rate").shouldBeBetween(0, 20),
                            (int) new AttributeValidator(section.getInt("FireMode.Burst"), "Burst rounds").shouldEqualOrBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("FireMode.Cooldown"), "Cooldown").shouldEqualOrBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("Reload.Duration"), "Reload duration").shouldBeHigherThan(0),
                            new AttributeValidator(section.getDouble("Accuracy"), "Accuracy").shouldBeBetween(0.0, 1.0),
                            getBulletProjectile(section),
                            FireMode.valueOf(section.getString("FireMode.Type")),
                            FireArmType.getValue(section.getString("GunType")),
                            ReloadType.valueOf(section.getString("Reload.Type")),
                            BattleSound.parseSoundArray(section.getString("Reload.Sound.Reload")),
                            BattleSound.parseSoundArray(section.getString("FireMode.ShootSound"))
                    );
                } catch (ValidationFailedException e) {
                    throw new ItemFormatException("Invalid item format " + section.getName() + ": " + e.getMessage());
                }
            }
        });
        list.add(new WeaponSerializer(FireArmType.LAUNCHER) {
            FireArm getFromSection(ConfigurationSection section) throws ItemFormatException {
                String name = section.getString("DisplayName");
                try {
                    return new BattleLauncher(
                            name,
                            getString("Description"),
                            new ItemStackBuilder(Material.valueOf(plugin.getBattlegroundsConfig().getWeaponMaterial("firearm"))).build(),
                            (short) new AttributeValidator(section.getInt("Durability"), "Durability").shouldBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("Ammo.Magazine"), "Magazine").shouldBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("Ammo.Start"), "Start ammunition").shouldBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("Ammo.Max"), "Max ammunition").shouldBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("FireMode.Cooldown"), "Cooldown").shouldEqualOrBeHigherThan(0),
                            (int) new AttributeValidator(section.getInt("Reload.Duration"), "Reload duration").shouldBeHigherThan(0),
                            new AttributeValidator(section.getDouble("Accuracy"), "Accuracy").shouldBeBetween(0.0, 1.0),
                            getLethalProjectile(section),
                            LaunchType.valueOf(section.getString("FireMode.LaunchType")),
                            ReloadType.valueOf(section.getString("Reload.Type")),
                            BattleSound.parseSoundArray(section.getString("Reload.Sound.Reload")),
                            BattleSound.parseSoundArray(section.getString("FireMode.ShootSound"))
                    );
                } catch (ValidationFailedException e) {
                    throw new ItemFormatException("Invalid item format " + section.getName() + ": " + e.getMessage());
                }
            }
        });
        return list;
    }

    private FireArm readFireArmConfiguration(ConfigurationSection section) throws IllegalArgumentException, ItemFormatException {
        String typeString = section.getString("GunType");
        FireArmType type = FireArmType.valueOf(typeString);
        for (WeaponSerializer serializer : serializers) {
            if (serializer.hasType(type)) {
                return (FireArm) serializer.getFromSection(section);
            }
        }
        throw new ItemFormatException("Invalid item format " + section.getName() + ": Unknown firearm type \"" + typeString + "\"");
    }

    private void setup() {
        fireArms = new HashMap<>();

        for (String fireArmName : getKeys(false)) {
            FireArm fireArm;
            try {
                fireArm = readFireArmConfiguration(getConfigurationSection(fireArmName));
            } catch (Exception e) {
                plugin.getLogger().severe(e.getMessage());
                continue;
            }
            fireArms.put(fireArm.getName(), fireArm);
        }
    }
}
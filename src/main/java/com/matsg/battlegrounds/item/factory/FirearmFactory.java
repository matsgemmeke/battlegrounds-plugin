package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.api.storage.ItemConfig;
import com.matsg.battlegrounds.api.item.Firearm;
import com.matsg.battlegrounds.api.item.ItemFactory;
import com.matsg.battlegrounds.api.item.ReloadType;
import com.matsg.battlegrounds.item.*;
import com.matsg.battlegrounds.util.BattleSound;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class FirearmFactory implements ItemFactory<Firearm> {

    private ItemConfig firearmConfig;

    public FirearmFactory(ItemConfig firearmConfig) {
        this.firearmConfig = firearmConfig;
    }

    public Firearm make(String id) {
        ConfigurationSection section = firearmConfig.getItemConfigurationSection(id);
        FirearmType firearmType;

        try {
            firearmType = FirearmType.valueOf(section.getString("FirearmType"));
        } catch (Exception e) {
            throw new FactoryCreationException(e.getMessage(), e);
        }

        String[] material = section.getString("Material").split(",");

        if (firearmType == FirearmType.ASSAULT_RIFLE
                || firearmType == FirearmType.HANDGUN
                || firearmType == FirearmType.LIGHT_MACHINE_GUN
                || firearmType == FirearmType.SHOTGUN
                || firearmType == FirearmType.SNIPER_RIFLE
                || firearmType == FirearmType.SUBMACHINE_GUN) {
            try {
                return new BattleGun(
                        section.getName(),
                        section.getString("DisplayName"),
                        section.getString("Description"),
                        new ItemStackBuilder(Material.valueOf(material[0])).setDurability(Short.valueOf(material[1])).build(),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Ammo.Magazine"), 0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("Ammo.Supply"), 0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("Ammo.Max"), 0),
                        AttributeValidator.shouldBeBetween(section.getInt("FireMode.FireRate"), 0, 20),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("FireMode.Burst"), 0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("FireMode.Cooldown"), 0),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Reload.Duration"), 0),
                        AttributeValidator.shouldBeBetween(section.getDouble("Accuracy"), 0.0, 1.0),
                        new Bullet(
                                AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Long.Damage"), 0.0),
                                AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Long.Distance"), 0.0),
                                AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Medium.Damage"), 0.0),
                                AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Medium.Distance"), 0.0),
                                AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Short.Damage"), 0.0),
                                AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Short.Distance"), 0.0),
                                AttributeValidator.shouldBeBetween(section.getDouble("HeadshotMultiplier"), 0.0, 100.0)
                        ),
                        firearmType,
                        FireMode.valueOf(section.getString("FireMode.Type")),
                        ReloadType.valueOf(section.getString("Reload.Type")),
                        BattleSound.parseSoundArray(section.getString("Reload.Sound.Reload")),
                        BattleSound.parseSoundArray(section.getString("Shot.ShotSound")),
                        BattleSound.parseSoundArray(section.getString("Shot.SuppressedSound")),
                        getCompatibleAttachments(section.getString("Attachments"))
                );
            } catch (ValidationFailedException e) {
                throw new FactoryCreationException(e.getMessage(), e);
            }
        } else {
            String[] projectileMaterial = section.getString("Projectile.Material").split(",");

            try {
                return new BattleLauncher(
                        section.getName(),
                        section.getString("DisplayName"),
                        section.getString("Description"),
                        new ItemStackBuilder(Material.valueOf(material[0])).setDurability(Short.valueOf(material[1])).build(),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Ammo.Magazine"), 0),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Ammo.Supply"), 0),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Ammo.Max"), 0),
                        AttributeValidator.shouldBeHigherThan(section.getDouble("Projectile.Speed"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("FireMode.Cooldown"), 0),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Reload.Duration"), 0),
                        AttributeValidator.shouldBeBetween(section.getDouble("Accuracy"), 0.0, 1.0),
                        new BattleLethal(
                                null,
                                null,
                                null,
                                new ItemStackBuilder(Material.valueOf(projectileMaterial[0])).setDurability(Short.parseShort(projectileMaterial[1])).build(),
                                0,
                                0,
                                AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Long.Damage"), 0.0),
                                AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Long.Distance"), 0.0),
                                AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Medium.Damage"), 0.0),
                                AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Medium.Distance"), 0.0),
                                AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Short.Damage"), 0.0),
                                AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Short.Distance"), 0.0),
                                0,
                                0,
                                null
                        ),
                        LaunchType.valueOf(section.getString("FireMode.LaunchType")),
                        ReloadType.valueOf(section.getString("Reload.Type")),
                        BattleSound.parseSoundArray(section.getString("Reload.Sound.Reload")),
                        BattleSound.parseSoundArray(section.getString("Shot.ShotSound"))
                );
            } catch (ValidationFailedException e) {
                throw new FactoryCreationException(e.getMessage(), e);
            }
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
}

package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.storage.ItemConfig;
import com.matsg.battlegrounds.item.*;
import com.matsg.battlegrounds.util.BattleSound;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirearmFactory implements ItemFactory<Firearm> {

    private static final int STAT_SYMBOL_LENGTH = 6;

    private Battlegrounds plugin;
    private ItemConfig firearmConfig;
    private Translator translator;

    public FirearmFactory(Battlegrounds plugin, ItemConfig firearmConfig, Translator translator) {
        this.plugin = plugin;
        this.firearmConfig = firearmConfig;
        this.translator = translator;
    }

    public Firearm make(String id) {
        ConfigurationSection section = firearmConfig.getItemConfigurationSection(id);
        FirearmType firearmType;

        try {
            firearmType = FirearmType.valueOf(section.getString("FirearmType"));
        } catch (Exception e) {
            throw new FactoryCreationException(e.getMessage(), e);
        }

        List<Material> piercableMaterials = new ArrayList<>();

        for (String material : plugin.getBattlegroundsConfig().pierceableMaterials) {
            piercableMaterials.add(Material.valueOf(material));
        }

        double accuracyAmplifier = plugin.getBattlegroundsConfig().firearmAccuracy;
        double damageAmplifier = plugin.getBattlegroundsConfig().firearmDamageModifer;

        EventDispatcher eventDispatcher = plugin.getEventDispatcher();
        Version version = plugin.getVersion();

        // Global firearm attributes
        ItemMetadata metadata = new ItemMetadata(id, section.getString("Description"), section.getString("DisplayName"));
        String[] material = section.getString("Material").split(",");

        if (firearmType == FirearmType.ASSAULT_RIFLE
                || firearmType == FirearmType.HANDGUN
                || firearmType == FirearmType.LIGHT_MACHINE_GUN
                || firearmType == FirearmType.SHOTGUN
                || firearmType == FirearmType.SNIPER_RIFLE
                || firearmType == FirearmType.SUBMACHINE_GUN) {

            try {
                double accuracy = AttributeValidator.shouldBeHigherThan(section.getInt("Ammo.Magazine"), 0);
                int cooldown = AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("FireMode.Cooldown"), 0);
                int fireRate = AttributeValidator.shouldBeBetween(section.getInt("FireMode.FireRate"), 0, 20);

                Bullet bullet = new Bullet(
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Long.Damage"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Long.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Medium.Damage"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Medium.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Short.Damage"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Short.Distance"), 0.0),
                        AttributeValidator.shouldBeBetween(section.getDouble("HeadshotMultiplier"), 0.0, 100.0)
                );

                String[] lore = new String[] {
                        ChatColor.WHITE + firearmType.getName(),
                        ChatColor.GRAY + formatFirearmStat(STAT_SYMBOL_LENGTH, accuracy * 100.0, 100.0) + " " + translator.translate(TranslationKey.STAT_ACCURACY),
                        ChatColor.GRAY + formatFirearmStat(STAT_SYMBOL_LENGTH, bullet.getShortDamage(), 55.0) + " " + translator.translate(TranslationKey.STAT_DAMAGE),
                        ChatColor.GRAY + formatFirearmStat(STAT_SYMBOL_LENGTH, Math.max((fireRate + 10 - cooldown / 2) * 10.0, 40.0), 200.0) + " " + translator.translate(TranslationKey.STAT_FIRERATE),
                        ChatColor.GRAY + formatFirearmStat(STAT_SYMBOL_LENGTH, bullet.getMidRange(), 70.0) + " " + translator.translate(TranslationKey.STAT_RANGE)
                };

                ItemStack itemStack = new ItemStackBuilder(new ItemStack(Material.valueOf(material[0])))
                        .setDisplayName(metadata.getName())
                        .setDurability(Short.valueOf(material[1]))
                        .setLore(lore)
                        .build();

                return new BattleGun(
                        metadata,
                        itemStack,
                        eventDispatcher,
                        version,
                        bullet,
                        firearmType,
                        FireMode.valueOf(section.getString("FireMode.Type")),
                        piercableMaterials,
                        getCompatibleAttachments(section.getString("Attachments")),
                        ReloadType.valueOf(section.getString("Reload.Type")),
                        BattleSound.parseSoundArray(section.getString("Reload.Sound.Reload")),
                        BattleSound.parseSoundArray(section.getString("Shot.SuppressedSound")),
                        BattleSound.parseSoundArray(section.getString("Shot.ShotSound")),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Ammo.Magazine"), 0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("Ammo.Supply"), 0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("Ammo.Max"), 0),
                        fireRate,
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("FireMode.Burst"), 0),
                        cooldown,
                        AttributeValidator.shouldBeHigherThan(section.getInt("Reload.Duration"), 0),
                        accuracy,
                        accuracyAmplifier,
                        damageAmplifier
                );
            } catch (ValidationFailedException e) {
                throw new FactoryCreationException(e.getMessage(), e);
            }
        } else {
            String[] projectileMaterial = section.getString("Projectile.Material").split(",");

            try {
                double accuracy = AttributeValidator.shouldBeHigherThan(section.getInt("Ammo.Magazine"), 0);
                int cooldown = AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("FireMode.Cooldown"), 0);

                Lethal lethal = new BattleLethal(
                        null,
                        null,
                        null,
                        new ItemStackBuilder(Material.valueOf(projectileMaterial[0])).setDurability(Short.parseShort(projectileMaterial[1])).build(),
                        null,
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
                );

                String[] lore = new String[] {
                        ChatColor.WHITE + firearmType.getName(),
                        ChatColor.GRAY + formatFirearmStat(STAT_SYMBOL_LENGTH, accuracy * 100.0, 100.0) + " " + translator.translate(TranslationKey.STAT_ACCURACY),
                        ChatColor.GRAY + formatFirearmStat(STAT_SYMBOL_LENGTH, lethal.getShortDamage(), 50.0) + " " + translator.translate(TranslationKey.STAT_DAMAGE),
                        ChatColor.GRAY + formatFirearmStat(STAT_SYMBOL_LENGTH, Math.max((15 - cooldown / 2) * 10.0, 40.0), 200.0) + " " + translator.translate(TranslationKey.STAT_FIRERATE),
                        ChatColor.GRAY + formatFirearmStat(STAT_SYMBOL_LENGTH, lethal.getLongRange(), 35.0) + " " + translator.translate(TranslationKey.STAT_RANGE)
                };

                ItemStack itemStack = new ItemStackBuilder(new ItemStack(Material.valueOf(material[0])))
                        .setDisplayName(metadata.getName())
                        .setDurability(Short.valueOf(material[1]))
                        .setLore(lore)
                        .build();

                return new BattleLauncher(
                        metadata,
                        itemStack,
                        eventDispatcher,
                        version,
                        LaunchType.valueOf(section.getString("FireMode.LaunchType")),
                        lethal,
                        piercableMaterials,
                        ReloadType.valueOf(section.getString("Reload.Type")),
                        BattleSound.parseSoundArray(section.getString("Reload.Sound.Reload")),
                        BattleSound.parseSoundArray(section.getString("Shot.ShotSound")),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Ammo.Magazine"), 0),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Ammo.Supply"), 0),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Ammo.Max"), 0),
                        AttributeValidator.shouldBeHigherThan(section.getDouble("Projectile.Speed"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("FireMode.Cooldown"), 0),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Reload.Duration"), 0),
                        accuracy,
                        accuracyAmplifier
                );
            } catch (ValidationFailedException e) {
                throw new FactoryCreationException(e.getMessage(), e);
            }
        }
    }

    private String formatFirearmStat(int length, double value, double max) {
        char symbol = '☗';
        char emptySymbol = '☖';

        if (value >= max) {
            return StringUtils.repeat(String.valueOf(symbol), length);
        }

        StringBuilder string = new StringBuilder();
        int a = (int) (Math.round(value / length) * length / (max / length)), i;

        for (i = 1; i <= a; i++) {
            string.append(symbol);
        }

        for (i = 1; i <= length - a; i++) {
            string.append(emptySymbol);
        }

        return string.toString();
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

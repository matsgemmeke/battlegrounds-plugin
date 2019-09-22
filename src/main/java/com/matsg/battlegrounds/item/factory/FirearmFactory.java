package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.storage.BattlegroundsConfig;
import com.matsg.battlegrounds.api.storage.ItemConfig;
import com.matsg.battlegrounds.item.*;
import com.matsg.battlegrounds.item.mechanism.*;
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

    private BattlegroundsConfig config;
    private EventDispatcher eventDispatcher;
    private FireModeFactory fireModeFactory;
    private ItemConfig firearmConfig;
    private LaunchSystemFactory launchSystemFactory;
    private ReloadSystemFactory reloadSystemFactory;
    private TaskRunner taskRunner;
    private Translator translator;
    private Version version;

    public FirearmFactory(
            ItemConfig firearmConfig,
            EventDispatcher eventDispatcher,
            FireModeFactory fireModeFactory,
            LaunchSystemFactory launchSystemFactory,
            ReloadSystemFactory reloadSystemFactory,
            TaskRunner taskRunner,
            Translator translator,
            Version version,
            BattlegroundsConfig config
    ) {
        this.firearmConfig = firearmConfig;
        this.eventDispatcher = eventDispatcher;
        this.fireModeFactory = fireModeFactory;
        this.launchSystemFactory = launchSystemFactory;
        this.reloadSystemFactory = reloadSystemFactory;
        this.taskRunner = taskRunner;
        this.translator = translator;
        this.version = version;
        this.config = config;
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

        for (String material : config.pierceableMaterials) {
            piercableMaterials.add(Material.valueOf(material));
        }

        double accuracyAmplifier = config.firearmAccuracy;
        double damageAmplifier = config.firearmDamageModifer;

        // Global firearm attributes
        ItemMetadata metadata = new ItemMetadata(id, section.getString("DisplayName"), section.getString("Description"));
        String[] material = section.getString("Material").split(",");

        ReloadSystem reloadSystem;
        ReloadSystemType reloadSystemType;

        try {
            reloadSystemType = ReloadSystemType.valueOf(section.getString("Reload.System"));
        } catch (Exception e) {
            throw new FactoryCreationException(e.getMessage(), e);
        }

        reloadSystem = reloadSystemFactory.make(reloadSystemType);

        if (firearmType == FirearmType.ASSAULT_RIFLE
                || firearmType == FirearmType.HANDGUN
                || firearmType == FirearmType.LIGHT_MACHINE_GUN
                || firearmType == FirearmType.SHOTGUN
                || firearmType == FirearmType.SNIPER_RIFLE
                || firearmType == FirearmType.SUBMACHINE_GUN) {

            try {
                double accuracy = AttributeValidator.shouldBeBetween(section.getDouble("Accuracy"), 0.0, 1.0);
                int cooldown = AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("FireMode.Cooldown"), 0);
                int fireRate = AttributeValidator.shouldBeBetween(section.getInt("FireMode.FireRate"), 0, 20);
                int burst = AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("FireMode.Burst"), 0);

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
                        .setDisplayName(ChatColor.WHITE + metadata.getName())
                        .setDurability(Short.valueOf(material[1]))
                        .setLore(lore)
                        .build();

                FireModeType fireModeType = FireModeType.valueOf(section.getString("FireMode.Type"));
                FireMode fireMode = fireModeFactory.make(fireModeType, fireRate, burst);

                Gun gun = new BattleGun(
                        metadata,
                        itemStack,
                        taskRunner,
                        version,
                        eventDispatcher,
                        bullet,
                        firearmType,
                        fireMode,
                        piercableMaterials,
                        getCompatibleAttachments(section.getString("Attachments")),
                        reloadSystem,
                        BattleSound.parseSoundArray(section.getString("Reload.Sound.Reload")),
                        BattleSound.parseSoundArray(section.getString("Shot.ShotSound")),
                        BattleSound.parseSoundArray(section.getString("Shot.SuppressedSound")),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Ammo.Magazine"), 0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("Ammo.Supply"), 0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("Ammo.Max"), 0),
                        fireRate,
                        burst,
                        cooldown,
                        AttributeValidator.shouldBeHigherThan(section.getInt("Reload.Duration"), 0),
                        accuracy,
                        accuracyAmplifier,
                        damageAmplifier
                );

                fireMode.setWeapon(gun);
                reloadSystem.setWeapon(gun);

                return gun;
            } catch (ValidationFailedException e) {
                throw new FactoryCreationException(e.getMessage(), e);
            }
        } else {
            String[] projectileMaterial = section.getString("Projectile.Material").split(",");

            try {
                double accuracy = AttributeValidator.shouldBeHigherThan(section.getInt("Ammo.Magazine"), 0);
                int cooldown = AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("FireMode.Cooldown"), 0);
                double launchSpeed = AttributeValidator.shouldBeHigherThan(section.getDouble("Projectile.Speed"), 0.0);

                Lethal lethal = new BattleLethal(
                        null,
                        new ItemStackBuilder(Material.valueOf(projectileMaterial[0])).setDurability(Short.parseShort(projectileMaterial[1])).build(),
                        null,
                        null,
                        null,
                        null,
                        0,
                        0,
                        0,
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Long.Damage"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Long.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Medium.Damage"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Medium.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Short.Damage"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Short.Distance"), 0.0),
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

                LaunchSystemType launchSystemType = LaunchSystemType.valueOf(section.getString("FireMode.LaunchSystem"));
                LaunchSystem launchSystem = launchSystemFactory.make(launchSystemType, launchSpeed);

                Launcher launcher = new BattleLauncher(
                        metadata,
                        itemStack,
                        eventDispatcher,
                        taskRunner,
                        version,
                        launchSystem,
                        lethal,
                        piercableMaterials,
                        reloadSystem,
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

                reloadSystem.setWeapon(launcher);

                return launcher;
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

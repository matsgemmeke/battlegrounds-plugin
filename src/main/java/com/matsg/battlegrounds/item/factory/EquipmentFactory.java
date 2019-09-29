package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.storage.ItemConfig;
import com.matsg.battlegrounds.item.*;
import com.matsg.battlegrounds.item.mechanism.IgnitionSystem;
import com.matsg.battlegrounds.item.mechanism.IgnitionSystemType;
import com.matsg.battlegrounds.item.mechanism.TacticalEffectType;
import com.matsg.battlegrounds.util.BattleSound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class EquipmentFactory implements ItemFactory<Equipment> {

    private EventDispatcher eventDispatcher;
    private IgnitionSystemFactory ignitionSystemFactory;
    private ItemConfig equipmentConfig;
    private TacticalEffectFactory tacticalEffectFactory;
    private TaskRunner taskRunner;
    private Translator translator;
    private Version version;

    public EquipmentFactory(
            ItemConfig equipmentConfig,
            EventDispatcher eventDispatcher,
            IgnitionSystemFactory ignitionSystemFactory,
            TacticalEffectFactory tacticalEffectFactory,
            TaskRunner taskRunner,
            Translator translator,
            Version version
    ) {
        this.equipmentConfig = equipmentConfig;
        this.eventDispatcher = eventDispatcher;
        this.ignitionSystemFactory = ignitionSystemFactory;
        this.tacticalEffectFactory = tacticalEffectFactory;
        this.taskRunner = taskRunner;
        this.translator = translator;
        this.version = version;
    }

    public Equipment make(String id) {
        ConfigurationSection section = equipmentConfig.getItemConfigurationSection(id);
        String[] material = section.getString("Material").split(",");
        EquipmentType equipmentType;

        try {
            equipmentType = EquipmentType.valueOf(section.getString("EquipmentType"));
        } catch (Exception e) {
            throw new FactoryCreationException(e.getMessage(), e);
        }

        String[] lore = new String[] {
                ChatColor.WHITE + translator.translate(equipmentType.getNameKey())
        };

        // Global equipment attributes
        ItemMetadata metadata = new ItemMetadata(id, section.getString("DisplayName"), section.getString("Description"));
        ItemStack itemStack = new ItemStackBuilder(Material.valueOf(material[0]))
                .setDisplayName(metadata.getName())
                .setDurability(Short.valueOf(material[1]))
                .setLore(lore)
                .build();

        if (equipmentType == EquipmentType.LETHAL) {
            try {
                int ignitionTime = AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("IgnitionTime"), 0);

                IgnitionSystemType ignitionSystemType = ignitionTime > 0 ? IgnitionSystemType.FUSE : IgnitionSystemType.TRIGGER;
                IgnitionSystem ignitionSystem = ignitionSystemFactory.make(ignitionSystemType);

                Lethal lethal = new BattleLethal(
                        metadata,
                        itemStack,
                        taskRunner,
                        version,
                        eventDispatcher,
                        ignitionSystem,
                        AttributeValidator.shouldBeHigherThan(section.getInt("Amount"), 0),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Cooldown"), 0),
                        ignitionTime,
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Long.Damage"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Long.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Medium.Damage"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Medium.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Short.Damage"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Short.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Velocity"), 0.0),
                        BattleSound.parseSoundArray(section.getString("Sound"))
                );

                ignitionSystem.setWeapon(lethal);

                return lethal;
            } catch (ValidationFailedException e) {
                throw new FactoryCreationException(e.getMessage(), e);
            }
        } else {
            try {
                int duration = AttributeValidator.shouldBeHigherThan(section.getInt("Duration"), 0);
                int ignitionTime = AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("IgnitionTime"), 0);

                IgnitionSystemType ignitionSystemType = ignitionTime > 0 ? IgnitionSystemType.FUSE : IgnitionSystemType.TRIGGER;
                IgnitionSystem ignitionSystem = ignitionSystemFactory.make(ignitionSystemType);

                TacticalEffectType tacticalEffectType = TacticalEffectType.valueOf(section.getString("Effect"));
                TacticalEffect tacticalEffect = tacticalEffectFactory.make(tacticalEffectType, duration);

                Tactical tactical = new BattleTactical(
                        metadata,
                        itemStack,
                        taskRunner,
                        version,
                        ignitionSystem,
                        BattleSound.parseSoundArray(section.getString("Sound")),
                        tacticalEffect,
                        AttributeValidator.shouldBeHigherThan(section.getInt("Amount"), 0),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Cooldown"), 0),
                        ignitionTime,
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Long.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Medium.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Short.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Velocity"), 0.0)
                );

                ignitionSystem.setWeapon(tactical);

                return tactical;
            } catch (ValidationFailedException e) {
                throw new FactoryCreationException(e.getMessage(), e);
            }
        }
    }
}

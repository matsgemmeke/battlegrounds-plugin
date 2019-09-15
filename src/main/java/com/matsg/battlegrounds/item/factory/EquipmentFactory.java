package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.item.ItemMetadata;
import com.matsg.battlegrounds.api.storage.ItemConfig;
import com.matsg.battlegrounds.api.item.Equipment;
import com.matsg.battlegrounds.api.item.ItemFactory;
import com.matsg.battlegrounds.item.*;
import com.matsg.battlegrounds.util.BattleSound;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class EquipmentFactory implements ItemFactory<Equipment> {

    private EventDispatcher eventDispatcher;
    private ItemConfig equipmentConfig;

    public EquipmentFactory(EventDispatcher eventDispatcher, ItemConfig equipmentConfig) {
        this.eventDispatcher = eventDispatcher;
        this.equipmentConfig = equipmentConfig;
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

        ItemMetadata metadata = new ItemMetadata(id, section.getString("DisplayName"), section.getString("Description"));
        ItemStack itemStack = new ItemStackBuilder(Material.valueOf(material[0]))
                .setDurability(Short.valueOf(material[1]))
                .build();

        if (equipmentType == EquipmentType.LETHAL) {
            try {
                int ignitionTime = AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("IgnitionTime"), 0);

                IgnitionType ignitionType = ignitionTime > 0 ? IgnitionType.AGGRESSIVE : IgnitionType.PASSIVE;

                return new BattleLethal(
                        metadata,
                        itemStack,
                        eventDispatcher,
                        ignitionType,
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
            } catch (ValidationFailedException e) {
                throw new FactoryCreationException(e.getMessage(), e);
            }
        } else {
            try {
                int ignitionTime = AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("IgnitionTime"), 0);

                IgnitionType ignitionType = ignitionTime > 0 ? IgnitionType.AGGRESSIVE : IgnitionType.PASSIVE;

                return new BattleTactical(
                        metadata,
                        itemStack,
                        ignitionType,
                        BattleSound.parseSoundArray(section.getString("Sound")),
                        BattleTacticalEffect.valueOf(section.getString("Effect")),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Amount"), 0),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Cooldown"), 0),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Duration"), 0),
                        ignitionTime,
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Long.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Medium.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Short.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Velocity"), 0.0)
                );
            } catch (ValidationFailedException e) {
                throw new FactoryCreationException(e.getMessage(), e);
            }
        }
    }
}

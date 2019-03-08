package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.api.config.ItemConfig;
import com.matsg.battlegrounds.api.item.Equipment;
import com.matsg.battlegrounds.api.item.ItemFactory;
import com.matsg.battlegrounds.item.*;
import com.matsg.battlegrounds.util.BattleSound;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class EquipmentFactory implements ItemFactory<Equipment> {

    private ItemConfig equipmentConfig;

    public EquipmentFactory(ItemConfig equipmentConfig) {
        this.equipmentConfig = equipmentConfig;
    }

    public Equipment make(String id) {
        ConfigurationSection section = equipmentConfig.getItemConfigurationSection(id);
        EquipmentType equipmentType;

        try {
            equipmentType = EquipmentType.valueOf(section.getString("EquipmentType"));
        } catch (Exception e) {
            throw new FactoryCreationException(e.getMessage(), e);
        }

        String[] material = section.getString("Material").split(",");

        if (equipmentType == EquipmentType.LETHAL) {
            try {
                return new BattleLethal(
                        section.getName(),
                        section.getString("DisplayName"),
                        section.getString("Description"),
                        new ItemStackBuilder(Material.valueOf(material[0])).build(),
                        AttributeValidator.shouldEqualOrBeHigherThan(Short.valueOf(material[1]), (short) 0),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Amount"), 0),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Cooldown"), 0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Long.Damage"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Long.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Medium.Damage"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Medium.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Short.Damage"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Short.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Velocity"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("IgnitionTime"), 0),
                        BattleSound.parseSoundArray(section.getString("Sound"))
                );
            } catch (ValidationFailedException e) {
                throw new FactoryCreationException(e.getMessage(), e);
            }
        } else {
            try {
                return new BattleTactical(
                        section.getName(),
                        section.getString("DisplayName"),
                        section.getString("Description"),
                        new ItemStackBuilder(Material.valueOf(material[0])).build(),
                        AttributeValidator.shouldEqualOrBeHigherThan(Short.valueOf(material[1]), (short) 0),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Amount"), 0),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Cooldown"), 0),
                        BattleTacticalEffect.valueOf(section.getString("Effect")),
                        AttributeValidator.shouldBeHigherThan(section.getInt("Duration"), 0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Long.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Medium.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Range.Short.Distance"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Velocity"), 0.0),
                        AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("IgnitionTime"), 0),
                        BattleSound.parseSoundArray(section.getString("Sound"))
                );
            } catch (ValidationFailedException e) {
                throw new FactoryCreationException(e.getMessage(), e);
            }
        }
    }
}

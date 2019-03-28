package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.api.storage.ItemConfig;
import com.matsg.battlegrounds.api.item.ItemFactory;
import com.matsg.battlegrounds.api.item.MeleeWeapon;
import com.matsg.battlegrounds.item.BattleMeleeWeapon;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class MeleeWeaponFactory implements ItemFactory<MeleeWeapon> {

    private ItemConfig meleeWeaponConfig;

    public MeleeWeaponFactory(ItemConfig meleeWeaponConfig) {
        this.meleeWeaponConfig = meleeWeaponConfig;
    }

    public MeleeWeapon make(String id) {
        ConfigurationSection section = meleeWeaponConfig.getItemConfigurationSection(id);
        String[] material = section.getString("Material").split(",");

        try {
            return new BattleMeleeWeapon(
                    section.getName(),
                    section.getString("DisplayName"),
                    section.getString("Description"),
                    new ItemStackBuilder(Material.valueOf(material[0])).setDurability(Short.valueOf(material[1])).build(),
                    AttributeValidator.shouldEqualOrBeHigherThan(section.getDouble("Damage"), 0.0),
                    AttributeValidator.shouldBeHigherThan(section.getInt("Amount"), 0),
                    section.getBoolean("Throwable"),
                    AttributeValidator.shouldEqualOrBeHigherThan(section.getInt("Cooldown"), 0)
            );
        } catch (ValidationFailedException e) {
            throw new FactoryCreationException(e.getMessage(), e);
        }
    }
}

package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.api.config.ItemConfig;
import com.matsg.battlegrounds.api.item.ItemFactory;
import com.matsg.battlegrounds.api.item.Knife;
import com.matsg.battlegrounds.item.BattleKnife;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class KnifeFactory implements ItemFactory<Knife> {

    private ItemConfig knifeConfig;

    public KnifeFactory(ItemConfig knifeConfig) {
        this.knifeConfig = knifeConfig;
    }

    public Knife make(String id) {
        ConfigurationSection section = knifeConfig.getItemConfigurationSection(id);
        String[] material = section.getString("Material").split(",");

        try {
            return new BattleKnife(
                    section.getName(),
                    section.getString("DisplayName"),
                    section.getString("Description"),
                    new ItemStackBuilder(Material.valueOf(material[0])).build(),
                    AttributeValidator.shouldEqualOrBeHigherThan(Short.valueOf(material[1]), (short) 0),
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

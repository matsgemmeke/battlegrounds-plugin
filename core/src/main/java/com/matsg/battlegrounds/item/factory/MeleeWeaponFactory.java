package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.item.ItemMetadata;
import com.matsg.battlegrounds.api.item.ItemType;
import com.matsg.battlegrounds.api.storage.ItemConfig;
import com.matsg.battlegrounds.api.item.ItemFactory;
import com.matsg.battlegrounds.api.item.MeleeWeapon;
import com.matsg.battlegrounds.item.BattleMeleeWeapon;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.item.MeleeWeaponItemType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class MeleeWeaponFactory implements ItemFactory<MeleeWeapon> {

    private EventDispatcher eventDispatcher;
    private InternalsProvider interals;
    private ItemConfig meleeWeaponConfig;
    private TaskRunner taskRunner;
    private Translator translator;

    public MeleeWeaponFactory(
            ItemConfig meleeWeaponConfig,
            EventDispatcher eventDispatcher,
            InternalsProvider internals,
            TaskRunner taskRunner,
            Translator translator
    ) {
        this.meleeWeaponConfig = meleeWeaponConfig;
        this.eventDispatcher = eventDispatcher;
        this.interals = interals;
        this.taskRunner = taskRunner;
        this.translator = translator;
    }

    public MeleeWeapon make(String id) {
        ConfigurationSection section = meleeWeaponConfig.getItemConfigurationSection(id);
        String[] material = section.getString("Material").split(",");

        ItemType itemType = new MeleeWeaponItemType(TranslationKey.ITEM_TYPE_MELEE_WEAPON.getPath());

        String[] lore = new String[] {
                ChatColor.WHITE + translator.translate(itemType.getNameKey())
        };

        ItemMetadata metadata = new ItemMetadata(id, section.getString("DisplayName"), section.getString("Description"));
        ItemStack itemStack = new ItemStackBuilder(Material.valueOf(material[0]))
                .setDisplayName(ChatColor.WHITE + metadata.getName())
                .setDurability(Short.valueOf(material[1]))
                .setLore(lore)
                .build();

        try {
            return new BattleMeleeWeapon(
                    metadata,
                    itemStack,
                    interals,
                    taskRunner,
                    eventDispatcher,
                    itemType,
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

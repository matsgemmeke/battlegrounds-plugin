package com.matsg.battlegrounds.mode.zombies.gui;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.ArenaComponent;
import com.matsg.battlegrounds.gui.Button;
import com.matsg.battlegrounds.gui.FunctionalButton;
import com.matsg.battlegrounds.gui.view.AbstractSettingsView;
import com.matsg.battlegrounds.gui.view.View;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ZombiesSettingsView extends AbstractSettingsView {

    private static final int INVENTORY_SIZE = 45;
    private static final String EMPTY_STRING = "";

    private Battlegrounds plugin;
    private Consumer<ArenaComponent> removeFunction;
    private Inventory inventory;
    private Translator translator;
    private View previousView;
    private Zombies zombies;

    public ZombiesSettingsView(
            Battlegrounds plugin,
            Zombies zombies,
            Consumer<ArenaComponent> removeFunction,
            Translator translator,
            View previousView
    ) {
        this.plugin = plugin;
        this.zombies = zombies;
        this.removeFunction = removeFunction;
        this.translator = translator;
        this.previousView = previousView;
        this.inventory = createInventory();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void refreshContent() {
        inventory.clear();

        for (Section section : zombies.getSectionContainer().getAll()) {
            ItemStack sectionItemStack = new ItemStackBuilder(new ItemStack(XMaterial.CHEST.parseMaterial()))
                    .setDisplayName(
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON.getPath(),
                                    new Placeholder("bg_component_id", section.getId())
                            )
                    )
                    .setLore(
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_TYPE.getPath(),
                                    new Placeholder("bg_component_type", "Section")
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_COMPONENTS.getPath(),
                                    new Placeholder("bg_component_components", section.getComponentCount())
                            ),
                            EMPTY_STRING,
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_FILTER.getPath()),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_REMOVE.getPath())
                    )
                    .build();

            View sectionView = new SectionSettingsView(plugin, section, removeFunction, translator, this);
            Consumer<Player> leftClick = player -> player.openInventory(sectionView.getInventory());
            Consumer<Player> rightClick = player -> {
                zombies.removeComponent(section);
                removeFunction.accept(section);
            };
            Button button = new FunctionalButton(leftClick, rightClick);

            addButton(sectionItemStack, button);

            inventory.addItem(sectionItemStack);
        }

        ItemStack backButton = new ItemStackBuilder(new ItemStack(XMaterial.COMPASS.parseMaterial()))
                .setDisplayName(translator.translate(TranslationKey.GO_BACK.getPath()))
                .build();

        createBackButton(backButton, previousView);

        inventory.setItem(INVENTORY_SIZE - 1, backButton);
    }

    private Inventory createInventory() {
        String title = translator.translate(TranslationKey.VIEW_ZOMBIES_SETTINGS_TITLE.getPath());
        inventory = plugin.getServer().createInventory(this, INVENTORY_SIZE, title);
        refreshContent();
        return inventory;
    }
}

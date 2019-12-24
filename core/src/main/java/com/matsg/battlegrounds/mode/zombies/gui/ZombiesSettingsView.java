package com.matsg.battlegrounds.mode.zombies.gui;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.ArenaComponent;
import com.matsg.battlegrounds.gui.ViewFactory;
import com.matsg.battlegrounds.gui.view.View;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ZombiesSettingsView implements View {

    private static final String EMPTY_STRING = "";

    public Gui gui;
    private Consumer<ArenaComponent> onComponentRemove;
    private Translator translator;
    private ViewFactory viewFactory;
    private View previousView;
    private Zombies gameMode;

    public ZombiesSettingsView setGameMode(Zombies gameMode) {
        this.gameMode = gameMode;
        return this;
    }

    public ZombiesSettingsView setOnComponentRemove(Consumer<ArenaComponent> onComponentRemove) {
        this.onComponentRemove = onComponentRemove;
        return this;
    }

    public ZombiesSettingsView setPreviousView(View previousView) {
        this.previousView = previousView;
        return this;
    }

    public ZombiesSettingsView setTranslator(Translator translator) {
        this.translator = translator;
        return this;
    }

    public ZombiesSettingsView setViewFactory(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
        return this;
    }

    public void backButtonClick(InventoryClickEvent event) {
        previousView.openInventory(event.getWhoClicked());
    }

    public void openInventory(HumanEntity entity) {
        gui.show(entity);
    }

    public void populateComponents(OutlinePane pane) {
        for (Section section : gameMode.getSectionContainer().getAll()) {
            ItemStack itemStack = new ItemStackBuilder(new ItemStack(XMaterial.ENDER_CHEST.parseMaterial()))
                    .setDisplayName(
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM.getPath(),
                                    new Placeholder("bg_component_id", section.getId())
                            )
                    )
                    .setLore(
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_TYPE.getPath(),
                                    new Placeholder("bg_component_type", "Section")
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_NAME.getPath(),
                                    new Placeholder("bg_component_name", section.getName())
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_COMPONENTS.getPath(),
                                    new Placeholder("bg_component_components", section.getComponentCount())
                            ),
                            EMPTY_STRING,
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_FILTER.getPath()),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_REMOVE.getPath())
                    )
                    .build();

            pane.addItem(new GuiItem(itemStack, event -> {
                if (event.getClick() == ClickType.LEFT) {
                     View view = viewFactory.make(SectionSettingsView.class, instance -> {
                         instance.setOnComponentRemove(onComponentRemove);
                         instance.setPreviousView(this);
                         instance.setSection(section);
                     });
                     view.openInventory(event.getWhoClicked());
                } else if (event.getClick() == ClickType.RIGHT) {
                    onComponentRemove.accept(section);
                    gameMode.removeComponent(section);
                    pane.clear();
                    populateComponents(pane);
                    gui.update();
                }
            }));
        }
    }
}

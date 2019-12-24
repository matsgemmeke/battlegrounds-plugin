package com.matsg.battlegrounds.gui.view;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.ArenaComponent;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ArenaSettingsView implements View {

    private static final String EMPTY_STRING = "";

    public Gui gui;
    private Arena arena;
    private Consumer<ArenaComponent> onComponentRemove;
    private Translator translator;
    private View previousView;

    public ArenaSettingsView setArena(Arena arena) {
        this.arena = arena;
        return this;
    }

    public ArenaSettingsView setOnComponentRemove(Consumer<ArenaComponent> onComponentRemove) {
        this.onComponentRemove = onComponentRemove;
        return this;
    }

    public ArenaSettingsView setPreviousView(View previousView) {
        this.previousView = previousView;
        return this;
    }

    public ArenaSettingsView setTranslator(Translator translator) {
        this.translator = translator;
        return this;
    }

    public void backButtonClick(InventoryClickEvent event) {
        previousView.openInventory(event.getWhoClicked());
    }

    public void openInventory(HumanEntity entity) {
        gui.show(entity);
    }

    public void populateComponents(OutlinePane pane) {
        for (Spawn spawn : arena.getSpawnContainer().getAll()) {
            Location location = spawn.getLocation();
            String locationString = "x" + location.getBlockX() + ", y" + location.getBlockY() + ", z" + location.getBlockZ();

            ItemStack itemStack = new ItemStackBuilder(new ItemStack(XMaterial.RED_BED.parseMaterial()))
                    .setDisplayName(
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM.getPath(),
                                    new Placeholder("bg_component_id", spawn.getId())
                            )
                    )
                    .setLore(
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_TYPE.getPath(),
                                    new Placeholder("bg_component_type", "Spawn")
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_LOCATION.getPath(),
                                    new Placeholder("bg_component_location", locationString)
                            ),
                            EMPTY_STRING,
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_TELEPORT.getPath()),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_REMOVE.getPath())
                    )
                    .build();

            pane.addItem(new GuiItem(itemStack, event -> {
                if (event.getClick() == ClickType.LEFT) {
                    event.getWhoClicked().teleport(spawn.getLocation());
                } else if (event.getClick() == ClickType.RIGHT) {
                    onComponentRemove.accept(spawn);
                    pane.clear();
                    populateComponents(pane);
                    gui.update();
                }
            }));
        }
    }
}

package com.matsg.battlegrounds.gui.view;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.gui.Button;
import com.matsg.battlegrounds.gui.FunctionalButton;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ArenaSettingsView extends AbstractSettingsView {

    private static final int INVENTORY_SIZE = 45;
    private static final String EMPTY_STRING = "";

    private Arena arena;
    private Battlegrounds plugin;
    private Inventory inventory;
    private Translator translator;
    private View previousView;

    public ArenaSettingsView(Battlegrounds plugin, Arena arena, Translator translator, View previousView) {
        this.plugin = plugin;
        this.arena = arena;
        this.translator = translator;
        this.previousView = previousView;
        this.inventory = createInventory();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void refreshContent() {
        inventory.clear();

        for (Spawn spawn : arena.getSpawnContainer().getAll()) {
            Location location = spawn.getLocation();
            String locationString = "x" + location.getBlockX() + ", y" + location.getBlockY() + ", z" + location.getBlockZ();

            ItemStack itemStack = new ItemStackBuilder(new ItemStack(XMaterial.RED_BED.parseMaterial()))
                    .setDisplayName(
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON.getPath(),
                                    new Placeholder("bg_component_id", spawn.getId())
                            )
                    )
                    .setLore(
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_TYPE.getPath(),
                                    new Placeholder("bg_component_type", "Spawn")
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_LOCATION.getPath(),
                                    new Placeholder("bg_component_location", locationString)
                            ),
                            EMPTY_STRING,
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_TELEPORT.getPath()),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_REMOVE.getPath())

                    )
                    .build();

            Consumer<Player> leftClick = player -> player.teleport(spawn.getLocation());
            Consumer<Player> rightClick = player -> arena.removeComponent(spawn);
            Button button = new FunctionalButton(leftClick, rightClick);

            addButton(itemStack, button);

            inventory.addItem(itemStack);
        }

        ItemStack backButton = new ItemStackBuilder(new ItemStack(XMaterial.COMPASS.parseMaterial()))
                .setDisplayName(translator.translate(TranslationKey.GO_BACK.getPath()))
                .build();

        createBackButton(backButton, previousView);

        inventory.setItem(INVENTORY_SIZE - 1, backButton);
    }

    private Inventory createInventory() {
        String title = translator.translate(TranslationKey.VIEW_ARENA_SETTINGS_TITLE.getPath(), new Placeholder("bg_arena", arena.getName()));
        inventory = plugin.getServer().createInventory(this, INVENTORY_SIZE, title);
        refreshContent();
        return inventory;
    }
}

package com.matsg.battlegrounds.gui.view;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameConfiguration;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EditGameConfigurationView extends AbstractSettingsView {

    private static final int INVENTORY_SIZE = 45;

    private Battlegrounds plugin;
    private Game game;
    private GameConfiguration configuration;
    private Inventory inventory;
    private Translator translator;
    private View previousView;

    public EditGameConfigurationView(Battlegrounds plugin, Game game, GameConfiguration configuration, Translator translator, View previousView) {
        this.plugin = plugin;
        this.game = game;
        this.configuration = configuration;
        this.translator = translator;
        this.previousView = previousView;
        this.inventory = createInventory();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void refreshContent() {
        inventory.clear();

        ItemStack backButtonItem = new ItemStackBuilder(new ItemStack(XMaterial.COMPASS.parseMaterial()))
                .setDisplayName(translator.translate(TranslationKey.GO_BACK.getPath()))
                .build();

        createBackButton(backButtonItem, previousView);

        inventory.setItem(INVENTORY_SIZE - 1, backButtonItem);
    }

    private Inventory createInventory() {
        String title = translator.translate(TranslationKey.VIEW_EDIT_GAME_CONFIGURATION_TITLE.getPath(), new Placeholder("bg_game", game.getId()));
        Inventory inventory = plugin.getServer().createInventory(this, INVENTORY_SIZE, title);
        refreshContent();
        return inventory;
    }
}

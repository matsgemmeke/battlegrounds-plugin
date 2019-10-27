package com.matsg.battlegrounds.gui.view;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import com.matsg.battlegrounds.gui.*;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.gui.ZombiesSettingsView;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class GameSettingsView extends AbstractSettingsView {

    private static final int INVENTORY_SIZE = 45;

    private Battlegrounds plugin;
    private Game game;
    private Inventory inventory;
    private Translator translator;
    private View previousView;

    public GameSettingsView(Battlegrounds plugin, Game game, Translator translator, View previousView) {
        this.plugin = plugin;
        this.game = game;
        this.translator = translator;
        this.previousView = previousView;
        this.inventory = createInventory();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void refreshContent() {
        inventory.clear();

        int i = 0;

        for (Arena arena : game.getArenaList()) {
            ItemStack itemStack = new ItemStackBuilder(new ItemStack(XMaterial.MAP.parseMaterial()))
                    .setDisplayName(ChatColor.GOLD + arena.getName())
                    .build();

            View arenaView = new ArenaSettingsView(plugin, arena, translator, this);
            Consumer<Player> click = player -> player.openInventory(arenaView.getInventory());
            Button button = new FunctionalButton(click, click);

            addButton(itemStack, button);

            inventory.setItem(i++, itemStack);
        }

        for (GameMode gameMode : game.getGameModeList()) {
            if (gameMode.getComponentCount() > 0) {
                ItemStack itemStack = new ItemStackBuilder(new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (byte) 3))
                        .setDisplayName(ChatColor.GOLD + gameMode.getName())
                        .build();

                Consumer<ArenaComponent> removeFunction = component -> {
                    Arena arena = findArenaOfComponent(component.getId());
                    CacheYaml dataFile = game.getDataFile();

                    dataFile.set("arena." + arena.getName() + ".component." + component.getId(), null);
                    dataFile.save();
                };

                View gameModeView;

                if (gameMode instanceof Zombies) {
                    gameModeView = new ZombiesSettingsView(plugin, (Zombies) gameMode, removeFunction, translator, previousView);
                } else {
                    throw new ViewCreationException("Can not create view of game mode " + gameMode.getName());
                }

                Consumer<Player> click = player -> player.openInventory(gameModeView.getInventory());
                Button button = new FunctionalButton(click, click);

                addButton(itemStack, button);

                inventory.addItem(itemStack);
            }
        }

        GameConfiguration configuration = game.getConfiguration();
        View editConfigurationView = new EditGameConfigurationView(plugin, game, configuration, translator, this);
        Consumer<Player> configClick = player -> player.openInventory(editConfigurationView.getInventory());

        ItemStack configButton = new ItemStackBuilder(new ItemStack(XMaterial.REDSTONE.parseMaterial()))
                .setDisplayName(translator.translate(TranslationKey.VIEW_GAME_SETTINGS_CONFIG.getPath()))
                .build();
        Button button = new FunctionalButton(configClick, configClick);

        ItemStack backButton = new ItemStackBuilder(new ItemStack(XMaterial.COMPASS.parseMaterial()))
                .setDisplayName(translator.translate(TranslationKey.GO_BACK.getPath()))
                .build();

        addButton(configButton, button);
        createBackButton(backButton, previousView);

        inventory.setItem(INVENTORY_SIZE - 2, configButton);
        inventory.setItem(INVENTORY_SIZE - 1, backButton);
    }

    private Arena findArenaOfComponent(int componentId) {
        CacheYaml dataFile = game.getDataFile();

        for (String arena : dataFile.getConfigurationSection("arena").getKeys(false)) {
            for (String idString : dataFile.getConfigurationSection("arena." + arena + ".component").getKeys(false)) {
                if (idString.equals(String.valueOf(componentId))) {
                    return game.getArena(arena);
                }
            }
        }

        return null;
    }

    private Inventory createInventory() {
        String title = translator.translate(TranslationKey.VIEW_GAME_SETTINGS_TITLE.getPath(), new Placeholder("bg_game", game.getId()));
        inventory = plugin.getServer().createInventory(this, INVENTORY_SIZE, title);
        refreshContent();
        return inventory;
    }
}

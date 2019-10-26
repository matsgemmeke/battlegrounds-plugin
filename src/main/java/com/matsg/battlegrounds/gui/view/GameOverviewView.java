package com.matsg.battlegrounds.gui.view;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.ArenaComponent;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import com.matsg.battlegrounds.gui.*;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.gui.ZombiesOverviewView;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class GameOverviewView extends AbstractOverviewView {

    private static final int INVENTORY_SIZE = 45;

    private Battlegrounds plugin;
    private Game game;
    private Inventory inventory;
    private Translator translator;
    private View previousView;

    public GameOverviewView(Battlegrounds plugin, Game game, Translator translator, View previousView) {
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

            View arenaView = new ArenaOverviewView(plugin, arena, translator, this);
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
                    gameModeView = new ZombiesOverviewView(plugin, (Zombies) gameMode, removeFunction, translator, previousView);
                } else {
                    throw new ViewCreationException("Can not create view of game mode " + gameMode.getName());
                }

                Consumer<Player> click = player -> player.openInventory(gameModeView.getInventory());
                Button button = new FunctionalButton(click, click);

                addButton(itemStack, button);

                inventory.addItem(itemStack);
            }
        }

        ItemStack backButton = new ItemStackBuilder(new ItemStack(XMaterial.COMPASS.parseMaterial()))
                .setDisplayName(translator.translate(TranslationKey.GO_BACK.getPath()))
                .build();

        inventory.setItem(INVENTORY_SIZE - 1, backButton);
    }

    public void returnToPreviousView(Player player) {
        player.openInventory(previousView.getInventory());
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
        String title = translator.translate(TranslationKey.VIEW_GAME_OVERVIEW_TITLE.getPath(), new Placeholder("bg_game", game.getId()));
        inventory = plugin.getServer().createInventory(this, INVENTORY_SIZE, title);
        refreshContent();
        return inventory;
    }
}

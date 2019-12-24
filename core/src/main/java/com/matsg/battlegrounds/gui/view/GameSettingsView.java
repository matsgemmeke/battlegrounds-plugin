package com.matsg.battlegrounds.gui.view;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import com.matsg.battlegrounds.gui.*;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.gui.ZombiesSettingsView;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class GameSettingsView implements View {

    public Gui gui;
    private Game game;
    private ViewFactory viewFactory;
    private View previousView;

    public GameSettingsView setGame(Game game) {
        this.game = game;
        return this;
    }

    public GameSettingsView setPreviousView(View previousView) {
        this.previousView = previousView;
        return this;
    }

    public GameSettingsView setViewFactory(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
        return this;
    }

    public void backButtonClick(InventoryClickEvent event) {
        previousView.openInventory(event.getWhoClicked());
    }

    public void configurationButtonClick(InventoryClickEvent event) {
        View view = viewFactory.make(EditGameConfigurationView.class, instance -> {
            instance.setGame(game);
            instance.setPreviousView(this);
        });
        view.openInventory(event.getWhoClicked());
    }

    public void openInventory(HumanEntity entity) {
        gui.show(entity);
    }

    public void populateArenas(OutlinePane pane) {
        for (Arena arena : game.getArenaList()) {
            ItemStack itemStack = new ItemStackBuilder(new ItemStack(XMaterial.MAP.parseMaterial()))
                    .setDisplayName(ChatColor.GOLD + arena.getName())
                    .build();

            Consumer<ArenaComponent> onComponentRemove = component -> {
                arena.removeComponent(component);

                CacheYaml dataFile = game.getDataFile();
                dataFile.set("arena." + arena.getName() + ".component." + component.getId(), null);
                dataFile.save();
            };

            pane.addItem(new GuiItem(itemStack, event -> {
                View view = viewFactory.make(ArenaSettingsView.class, instance -> {
                    instance.setArena(arena);
                    instance.setOnComponentRemove(onComponentRemove);
                    instance.setPreviousView(this);
                });
                view.openInventory(event.getWhoClicked());
            }));
        }
    }

    public void populateGameModes(OutlinePane pane) {
        for (GameMode gameMode : game.getGameModeList()) {
            if (gameMode.getComponentCount() > 0) {
                ItemStack itemStack = new ItemStackBuilder(new ItemStack(XMaterial.CHEST.parseMaterial(), 1, (byte) 3))
                        .setDisplayName(ChatColor.GOLD + gameMode.getName())
                        .build();

                Consumer<ArenaComponent> onComponentRemove = component -> {
                    Arena arena = findArenaOfComponent(component.getId());
                    arena.removeComponent(component);

                    CacheYaml dataFile = game.getDataFile();
                    dataFile.set("arena." + arena.getName() + ".component." + component.getId(), null);
                    dataFile.save();
                };

                View gameModeView;

                if (gameMode instanceof Zombies) {
                    gameModeView = viewFactory.make(ZombiesSettingsView.class, instance -> {
                        instance.setGameMode((Zombies) gameMode);
                        instance.setOnComponentRemove(onComponentRemove);
                        instance.setPreviousView(this);
                    });
                } else {
                    throw new ViewCreationException("Can not create view of game mode " + gameMode.getName());
                }

                pane.addItem(new GuiItem(itemStack, event -> gameModeView.openInventory(event.getWhoClicked())));
            }
        }
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
}

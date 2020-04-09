package com.matsg.battlegrounds.gui.view;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.gui.ViewFactory;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

public class PluginSettingsView implements View {

    private static final String EMPTY_STRING = "";

    public Gui gui;
    private GameManager gameManager;
    private Translator translator;
    private ViewFactory viewFactory;

    public PluginSettingsView setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
        return this;
    }

    public PluginSettingsView setTranslator(Translator translator) {
        this.translator = translator;
        return this;
    }

    public PluginSettingsView setViewFactory(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
        return this;
    }

    public void openInventory(HumanEntity entity) {
        gui.show(entity);
    }

    public void populateGames(OutlinePane pane) {
        for (Game game : gameManager.getGames()) {
            ItemStack itemStack = new ItemStackBuilder(game.getState().toItemStack())
                    .setDisplayName(
                            translator.translate(TranslationKey.VIEW_PLUGIN_SETTINGS_GAME.getPath(),
                                    new Placeholder("bg_game", game.getId())
                            )
                    )
                    .setLore(
                            translator.translate(TranslationKey.VIEW_PLUGIN_SETTINGS_GAME_ARENA.getPath(),
                                    new Placeholder("bg_arena", game.getArena() != null ? game.getArena().getName() : "N/A")
                            ),
                            translator.translate(TranslationKey.VIEW_PLUGIN_SETTINGS_GAME_GAMEMODE.getPath(),
                                    new Placeholder("bg_gamemode", game.getGameMode().getName())
                            ),
                            translator.translate(TranslationKey.VIEW_PLUGIN_SETTINGS_GAME_PLAYERS.getPath(),
                                    new Placeholder("bg_maxplayers", game.getConfiguration().getMaxPlayers()),
                                    new Placeholder("bg_players", game.getPlayerManager().getPlayers().size())
                            ),
                            translator.translate(TranslationKey.VIEW_PLUGIN_SETTINGS_GAME_STATE.getPath(),
                                    new Placeholder("bg_state", game.getState())
                            ),
                            EMPTY_STRING,
                            translator.translate(TranslationKey.VIEW_PLUGIN_SETTINGS_GAME_SETTINGS.getPath())
                    )
                    .build();

            pane.addItem(new GuiItem(itemStack, event -> {
                View view = viewFactory.make(GameSettingsView.class, instance -> {
                    instance.setGame(game);
                    instance.setPreviousView(this);
                });
                view.openInventory(event.getWhoClicked());
            }));
        }
    }
}

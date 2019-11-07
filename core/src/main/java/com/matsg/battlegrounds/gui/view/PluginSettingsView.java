package com.matsg.battlegrounds.gui.view;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class PluginSettingsView implements View {

    private static final int INVENTORY_SIZE = 45;
    private static final long REFRESH_TASK_DELAY = 0;
    private static final long REFRESH_TASK_PERIOD = 200;
    private static final String EMPTY_STRING = "";

    private Battlegrounds plugin;
    private BukkitTask task;
    private Inventory inventory;
    private Map<ItemStack, Game> games;
    private Translator translator;

    public PluginSettingsView(Battlegrounds plugin, TaskRunner taskRunner, Translator translator) {
        this.plugin = plugin;
        this.translator = translator;
        this.games = new HashMap<>();
        this.inventory = createInventory();

        task = taskRunner.runTaskTimer(new BukkitRunnable() {
            public void run() {
                // Remove all icons from previous game states
                inventory.clear();

                for (Game game : plugin.getGameManager().getGames()) {
                    ItemStack itemStack = new ItemStackBuilder(game.getState().toItemStack())
                            .setDisplayName(
                                    translator.translate(TranslationKey.VIEW_PLUGIN_SETTINGS_GAME.getPath(),
                                            new Placeholder("bg_game", game.getId())
                                    )
                            )
                            .setLore(
                                    translator.translate(TranslationKey.VIEW_PLUGIN_SETTINGS_GAME_ARENA.getPath(),
                                            new Placeholder("bg_arena", game.getArena().getName())
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

                    inventory.addItem(itemStack);
                    games.put(itemStack, game);
                }
            }
        }, REFRESH_TASK_DELAY, REFRESH_TASK_PERIOD);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        if (itemStack == null) {
            return;
        }

        Game game = games.get(itemStack);

        if (game == null) {
            return;
        }

        View view = new GameSettingsView(plugin, game, translator, this);

        player.openInventory(view.getInventory());
    }

    public void onClose(Player player) {
        task.cancel();
    }

    private Inventory createInventory() {
        String title = translator.translate(TranslationKey.VIEW_PLUGIN_SETTINGS_TITLE.getPath());

        return plugin.getServer().createInventory(this, INVENTORY_SIZE, title);
    }
}

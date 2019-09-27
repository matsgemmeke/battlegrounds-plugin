package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GameOverviewView implements View {

    private static final int INVENTORY_SIZE = 45;

    private Battlegrounds plugin;
    private Game game;
    private Inventory inventory;
    private Map<ItemStack, Arena> arenas;
    private Translator translator;
    private View previousView;

    public GameOverviewView(Battlegrounds plugin, Game game, Translator translator, View previousView) {
        this.plugin = plugin;
        this.game = game;
        this.translator = translator;
        this.previousView = previousView;
        this.arenas = new HashMap<>();
        this.inventory = createInventory();

        for (Arena arena : game.getArenaList()) {
            ItemStack itemStack = new ItemStackBuilder(new ItemStack(XMaterial.MAP.parseMaterial()))
                    .setDisplayName(ChatColor.GOLD + arena.getName())
                    .build();

            inventory.addItem(itemStack);
            arenas.put(itemStack, arena);
        }

        ItemStack backButton = new ItemStackBuilder(new ItemStack(XMaterial.COMPASS.parseMaterial()))
                .setDisplayName(translator.translate(TranslationKey.GO_BACK.getPath()))
                .build();

        inventory.setItem(INVENTORY_SIZE - 1, backButton);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        if (itemStack == null) {
            return;
        }

        if (itemStack.getType() == Material.COMPASS) {
            player.openInventory(previousView.getInventory());
            return;
        }

        Arena arena = arenas.get(itemStack);

        if (arena == null) {
            return;
        }

        System.out.println(arena.getName());
    }

    public boolean onClose() {
        return true;
    }

    private Inventory createInventory() {
        String title = translator.translate(TranslationKey.VIEW_GAME_OVERVIEW_TITLE.getPath(), new Placeholder("bg_game", game.getId()));

        return plugin.getServer().createInventory(this, INVENTORY_SIZE, title);
    }
}

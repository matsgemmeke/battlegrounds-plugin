package com.matsg.battlegrounds.gui.view;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.mode.GameModeType;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectGameModeView implements View {

    private static final int INVENTORY_SIZE = 45;

    private Battlegrounds plugin;
    private Game game;
    private Inventory inventory;
    private List<GameMode> selected;
    private Map<ItemStack, GameMode> gameModes;
    private Translator translator;

    public SelectGameModeView(Battlegrounds plugin, Game game, Translator translator) {
        this.plugin = plugin;
        this.game = game;
        this.translator = translator;
        this.gameModes = new HashMap<>();
        this.inventory = createInventory();
        this.selected = new ArrayList<>();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }

        GameMode gameMode = gameModes.get(itemStack);

        if (gameMode == null) {
            return;
        }


    }

    public boolean onClose() {
        return true;
    }

    private Inventory createInventory() {
        String title = translator.translate(TranslationKey.VIEW_SELECT_GAMEMODE_TITLE.getPath(), new Placeholder("bg_game", game.getId()));

        // Add all gamemode types to the inventory
        for (GameModeType gameModeType : GameModeType.values()) {
            ItemStack itemStack = new ItemStackBuilder(new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (byte) 3))
                    .setDisplayName(ChatColor.GOLD)
        }

        return plugin.getServer().createInventory(this, INVENTORY_SIZE, title);
    }
}

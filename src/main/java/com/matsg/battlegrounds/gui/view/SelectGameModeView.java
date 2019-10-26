package com.matsg.battlegrounds.gui.view;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.mode.GameModeFactory;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectGameModeView implements View {

    private static final int INVENTORY_SIZE = 45;

    private Battlegrounds plugin;
    private Game game;
    private GameModeFactory gameModeFactory;
    private Inventory inventory;
    private List<GameModeType> selected;
    private Map<ItemStack, GameModeType> gameModeTypes;
    private Translator translator;

    public SelectGameModeView(Battlegrounds plugin, Game game, GameModeFactory gameModeFactory, Translator translator) {
        this.plugin = plugin;
        this.game = game;
        this.gameModeFactory = gameModeFactory;
        this.translator = translator;
        this.gameModeTypes = new HashMap<>();
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

        GameModeType gameModeType = gameModeTypes.get(itemStack);

        if (gameModeType == null || selected.contains(gameModeType)) {
            return;
        }

        GameMode gameMode = gameModeFactory.make(game, gameModeType);

        game.getConfiguration().getGameModeTypes().add(gameModeType.toString());
        game.setGameMode(gameMode);

        selected.add(gameModeType);

        refreshInventory(inventory);
    }

    public boolean onClose() {
        return true;
    }

    private Inventory createInventory() {
        String title = translator.translate(TranslationKey.VIEW_SELECT_GAMEMODE_TITLE.getPath(), new Placeholder("bg_game", game.getId()));
        Inventory inventory = plugin.getServer().createInventory(this, INVENTORY_SIZE, title);
        refreshInventory(inventory);
        return inventory;
    }

    private void refreshInventory(Inventory inventory) {
        inventory.clear();

        // Add all gamemode types to the inventory
        for (GameModeType gameModeType : GameModeType.values()) {
            List<String> lore = new ArrayList<>();
            lore.add(" ");
            int maxLength = 30;

            Pattern pattern = Pattern.compile("\\G\\s*(.{1," + maxLength + "})(?=\\s|$)", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(translator.translate(gameModeType.getDescriptionPath()));

            while (matcher.find()) {
                lore.add(ChatColor.WHITE + matcher.group(1));
            }

            ItemStack itemStack = new ItemStackBuilder(new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (byte) 3))
                    .setDisplayName(ChatColor.GOLD + translator.translate(gameModeType.getNamePath()))
                    .setLore(lore.toArray(new String[lore.size()]))
                    .build();

            gameModeTypes.put(itemStack, gameModeType);

            inventory.addItem(itemStack);
        }
    }
}

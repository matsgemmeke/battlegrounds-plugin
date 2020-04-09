package com.matsg.battlegrounds.gui.view;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.mode.GameModeFactory;
import com.matsg.battlegrounds.mode.GameModeType;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectGameModesView implements View {

    public Gui gui;
    private Game game;
    private GameModeFactory gameModeFactory;
    private Translator translator;
    private View previousView;

    public SelectGameModesView setGame(Game game) {
        this.game = game;
        return this;
    }

    public SelectGameModesView setGameModeFactory(GameModeFactory gameModeFactory) {
        this.gameModeFactory = gameModeFactory;
        return this;
    }

    public SelectGameModesView setPreviousView(View previousView) {
        this.previousView = previousView;
        return this;
    }

    public SelectGameModesView setTranslator(Translator translator) {
        this.translator = translator;
        return this;
    }

    public void backButtonClick(InventoryClickEvent event) {
        previousView.openInventory(event.getWhoClicked());
    }

    public void openInventory(HumanEntity entity) {
        gui.show(entity);
    }

    public void populateGameModes(OutlinePane pane) {
        for (GameModeType gameModeType : GameModeType.values()) {
            List<String> lore = new ArrayList<>();
            int maxLength = 30;

            Pattern pattern = Pattern.compile("\\G\\s*(.{1," + maxLength + "})(?=\\s|$)", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(translator.translate(gameModeType.getDescriptionPath()));

            while (matcher.find()) {
                lore.add(ChatColor.WHITE + matcher.group(1));
            }

            boolean selected = game.getConfiguration().getGameModeTypes().contains(gameModeType.toString());

            lore.add(" ");
            lore.add(selected ? ChatColor.GREEN + "Selected" : ChatColor.RED + "Unselected");

            ItemStack itemStack = new ItemStackBuilder(new ItemStack(XMaterial.CHEST.parseMaterial(), 1, (byte) 3))
                    .addItemFlags(ItemFlag.values())
                    .setDisplayName(ChatColor.GOLD + translator.translate(gameModeType.getNamePath()))
                    .setLore(lore)
                    .build();

            pane.addItem(new GuiItem(itemStack, event -> {
                event.setCancelled(true);

                GameMode gameMode = gameModeFactory.make(game, gameModeType);

                if (event.getClick() == ClickType.LEFT && !selected) {
                    game.getConfiguration().getGameModeTypes().add(gameModeType.toString());
                    game.getConfiguration().saveConfiguration(game.getDataFile());
                    game.getGameModeList().add(gameMode);
                    game.activateGameMode(gameMode);
                } else if (event.getClick() == ClickType.RIGHT && selected && game.getGameModeList().size() > 1) {
                    game.getConfiguration().getGameModeTypes().remove(gameModeType.toString());
                    game.getConfiguration().saveConfiguration(game.getDataFile());
                    game.getGameModeList().remove(game.getGameMode(gameMode.getClass()));

                    if (game.getGameMode().getClass() == gameMode.getClass()) {
                        game.activateGameMode(game.getGameModeList().get(0));
                    }
                }

                game.getGameSign().update();
                pane.clear();
                populateGameModes(pane);
                gui.update();
            }));
        }
    }
}

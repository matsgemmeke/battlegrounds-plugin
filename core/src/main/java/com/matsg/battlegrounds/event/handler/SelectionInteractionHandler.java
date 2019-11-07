package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattleSelection;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.*;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.util.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SelectionInteractionHandler implements EventHandler<PlayerInteractEvent> {

    private Battlegrounds plugin;
    private Translator translator;

    public SelectionInteractionHandler(Battlegrounds plugin, Translator translator) {
        this.plugin = plugin;
        this.translator = translator;
    }

    public void handle(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("battlegrounds.select")
                || event.getClickedBlock() == null
                || event.getItem() == null
                || event.getItem().getType() != Material.STICK) {
            return;
        }

        event.setCancelled(true);

        Location location = event.getClickedBlock().getLocation();
        SelectionManager selectionManager = plugin.getSelectionManager();

        Selection selection = selectionManager.getSelection(player);

        if (selection == null) {
            selection = new BattleSelection(new Pair<>(null, null), player.getWorld());
            selectionManager.setSelection(player, selection);
        }

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            selectionManager.setSelection(player, new BattleSelection(new Pair<>(location, selection.getSecondSelectedPoint()), selection.getWorld()));

            player.sendMessage(translator.translate(TranslationKey.SELECTION_SET.getPath(),
                    new Placeholder("bg_selection_pos", "First"),
                    new Placeholder("bg_selection_x", location.getX()),
                    new Placeholder("bg_selection_y", location.getY()),
                    new Placeholder("bg_selection_z", location.getZ())
            ));
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            selectionManager.setSelection(player, new BattleSelection(new Pair<>(selection.getFirstSelectedPoint(), location), selection.getWorld()));

            player.sendMessage(translator.translate(TranslationKey.SELECTION_SET.getPath(),
                    new Placeholder("bg_selection_pos", "Second"),
                    new Placeholder("bg_selection_x", location.getX()),
                    new Placeholder("bg_selection_y", location.getY()),
                    new Placeholder("bg_selection_z", location.getZ())
            ));
        }
    }
}

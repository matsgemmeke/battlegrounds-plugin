package com.matsg.battlegrounds.listener;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.gui.View;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class BattleEventHandler implements EventHandler {

    private Battlegrounds plugin;

    public BattleEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
        plugin.getEventManager().registerEventHandler(this);
    }

    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.getPlayerStorage().contains(player.getUniqueId())) {
            return;
        }
        plugin.getPlayerStorage().registerPlayer(player.getUniqueId(), player.getName());
    }

    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null || (game.getArena() != null && game.getArena().contains(player.getLocation()))) {
            return;
        }

        player.teleport(player.getLocation().add(event.getFrom().toVector().subtract(event.getTo().toVector()).normalize()));
    }

    public void onViewItemClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getHolder() instanceof View) {
            event.setCancelled(true);

            ((View) event.getInventory().getHolder()).onClick(player, item, event.getClick());
        }
    }
}
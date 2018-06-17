package com.matsg.battlegrounds.listener;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.gui.View;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class BattleEventHandler implements EventHandler {

    private Battlegrounds plugin;

    public BattleEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
        plugin.getEventManager().registerEventHandler(this);
    }

    private boolean isPlaying(Player player) {
        return plugin.getGameManager().getGame(player) != null;
    }

    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(isPlaying(event.getPlayer()) || plugin.getGameManager().getArena(event.getBlock().getLocation()) != null && plugin.getBattlegroundsConfig().arenaProtection);
    }

    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(isPlaying(event.getPlayer()) || plugin.getGameManager().getArena(event.getBlock().getLocation()) != null && plugin.getBattlegroundsConfig().arenaProtection);
    }

    public void onBlockUpdate(BlockPhysicsEvent event) {
        event.setCancelled(plugin.getGameManager().getArena(event.getBlock().getLocation()) != null && plugin.getBattlegroundsConfig().arenaProtection);
    }

    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getPlayerStorage().contains(player.getUniqueId())) {
            plugin.getPlayerStorage().registerPlayer(player.getUniqueId(), player.getName());
        }
        plugin.getPlayerStorage().updatePlayer(player);
    }

    public void onPlayerKick(PlayerKickEvent event) {
        onPlayerLeave(event.getPlayer());
    }

    private void onPlayerLeave(Player player) {
        if (!isPlaying(player)) {
            return;
        }
        Game game = plugin.getGameManager().getGame(player);
        game.getPlayerManager().removePlayer(game.getPlayerManager().getGamePlayer(player));
    }

    public void onPlayerQuit(PlayerQuitEvent event) {
        onPlayerLeave(event.getPlayer());
    }

    public void onViewItemClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getHolder() instanceof View) {
            event.setCancelled(true);

            ((View) event.getInventory().getHolder()).onClick(player, item, event.getClick());
        }
    }

    public void onViewItemClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof View && !((View) event.getInventory().getHolder()).onClose()) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    event.getPlayer().openInventory(event.getInventory());
                }
            }, 1);
        }
    }
}
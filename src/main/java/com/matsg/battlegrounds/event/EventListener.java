package com.matsg.battlegrounds.event;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.gui.View;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class EventListener implements Listener {

    private Battlegrounds plugin;

    public EventListener(Battlegrounds plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private boolean isPlaying(Player player) {
        return plugin.getGameManager().getGame(player) != null;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.getBattlegroundsConfig().arenaProtection && !isPlaying(event.getPlayer()) || plugin.getGameManager().getArena(event.getBlock().getLocation()) == null) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!plugin.getBattlegroundsConfig().arenaProtection && !isPlaying(event.getPlayer()) || plugin.getGameManager().getArena(event.getBlock().getLocation()) == null) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockUpdate(BlockPhysicsEvent event) {
        event.setCancelled(plugin.getGameManager().getArena(event.getBlock().getLocation()) != null);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getPlayerStorage().contains(player.getUniqueId())) {
            plugin.getPlayerStorage().registerPlayer(player.getUniqueId(), player.getName());
        }
        plugin.getPlayerStorage().updatePlayer(player);
    }

    @EventHandler
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

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        onPlayerLeave(event.getPlayer());
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || !(event.getClickedBlock().getState() instanceof Sign)) {
            return;
        }

        Sign sign = (Sign) event.getClickedBlock().getState();

        for (Game game : plugin.getGameManager().getGames()) {
            if (game.getGameSign() != null && game.getGameSign().getSign().equals(sign)) {
                game.getGameSign().click(event.getPlayer());
                break;
            }
        }
    }

    @EventHandler
    public void onViewItemClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getHolder() instanceof View) {
            event.setCancelled(true);

            ((View) event.getInventory().getHolder()).onClick(player, itemStack, event.getClick());
        }
    }

    @EventHandler
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
package com.matsg.battlegrounds.listener;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.gui.View;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BattleEventHandler implements EventHandler {

    private Battlegrounds plugin;

    public BattleEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
        plugin.getEventManager().registerEventHandler(this);
    }

    public void onCommandSend(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        List<String> list = plugin.getBattlegroundsConfig().allowedCommands;

        if (plugin.getGameManager().getGame(player) == null || player.isOp() || list.contains("*")
                || list.contains(event.getMessage().split(" ")[0].substring(1, event.getMessage().split(" ")[0].length()))) {
            return;
        }

        event.setCancelled(true);

        EnumMessage.COMMAND_NOT_ALLOWED.send(player);
    }

    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null || game.getArena() == null || !game.getState().isAllowItems()) {
            return;
        }

        Item item = game.getItemRegistry().getItemIgnoreMetadata(player.getInventory().getItemInMainHand());

        if (item == null) {
            return;
        }

        event.setCancelled(true);
        game.getItemRegistry().interact(item, event.getAction());
    }

    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.getPlayerStorage().contains(player.getUniqueId())) {
            return;
        }
        plugin.getPlayerStorage().registerPlayer(player.getUniqueId(), player.getName());
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
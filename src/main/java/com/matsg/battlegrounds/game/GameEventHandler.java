package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;

public class GameEventHandler implements EventHandler {

    private Battlegrounds plugin;

    public GameEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
        plugin.getEventManager().registerEventHandler(this);
    }

    private boolean isPlaying(Player player) {
        return plugin.getGameManager().getGame(player) != null;
    }

    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(isPlaying(event.getPlayer()) || (plugin.getGameManager().getArena(event.getBlock().getLocation()) != null && plugin.getBattlegroundsConfig().arenaProtection));
    }

    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(isPlaying(event.getPlayer()) || (plugin.getGameManager().getArena(event.getBlock().getLocation()) != null && plugin.getBattlegroundsConfig().arenaProtection));
    }

    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null) {
            for (GamePlayer gamePlayer : plugin.getGameManager().getAllPlayers()) {
                event.getRecipients().remove(gamePlayer.getPlayer());
            }
            return;
        }

        event.setCancelled(true);

        game.broadcastMessage(EnumMessage.PLAYER_MESSAGE.getMessage(
                new Placeholder("player_name", player.getName()),
                new Placeholder("bg_message", event.getMessage())));

        if (plugin.getBattlegroundsConfig().broadcastChat) {
            plugin.getLogger().info("[Game " + game.getId() + "] " + player.getName() + ": " + event.getMessage());
        }
    }

    public void onItemSwitch(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null) {
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Weapon weapon = gamePlayer.getLoadoutClass().getWeapon(player.getInventory().getItemInMainHand());

        if (weapon == null) {
            return;
        }

        weapon.onSwitch();
    }

    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null || game.getArena() == null || !game.getState().isInProgress()) {
            return;
        }

        event.setCancelled(true);

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Weapon weapon = game.getItemRegistry().getWeapon(player, player.getInventory().getItemInMainHand());

        if (gamePlayer == null || !gamePlayer.getStatus().canInteract() || weapon == null) {
            return;
        }

        game.getItemRegistry().interact(weapon, event.getAction());
    }

    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null || (game.getArena() != null && game.getArena().contains(player.getLocation()))) {
            return;
        }

        player.teleport(player.getLocation().add(event.getFrom().toVector().subtract(event.getTo().toVector()).normalize()));
    }
}
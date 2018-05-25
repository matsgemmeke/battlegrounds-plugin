package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.event.GamePlayerKillPlayerEvent;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.Knife;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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

    public void onGamePlayerDeath(GamePlayerDeathEvent event) {
        event.getGame().broadcastMessage(event.getDeathCause().getDeathMessage());
    }

    public void onGamePlayerKill(GamePlayerKillPlayerEvent event) {
        event.getGame().getGameMode().onKill(event.getGamePlayer(), event.getKiller(), event.getWeapon());
    }

    public void onItemSwitch(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null || !game.getState().isInProgress()) {
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Weapon weapon = gamePlayer.getLoadout().getWeapon(player.getInventory().getItemInMainHand());

        if (weapon == null) {
            return;
        }

        weapon.onSwitch();
    }

    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Game game = plugin.getGameManager().getGame(player);

        event.setCancelled(game != null && !game.getState().isInProgress());
    }

    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Game game = plugin.getGameManager().getGame(player);
        boolean differentGame = plugin.getGameManager().getGame((Player) event.getDamager()) != game;

        if (game == null || differentGame) {
            event.setCancelled(differentGame);
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player), damager = game.getPlayerManager().getGamePlayer((Player) event.getDamager());

        if (gamePlayer == null || damager == null || damager.getLoadout() == null) {
            event.setCancelled(true);
            return;
        }

        Knife knife = (Knife) damager.getLoadout().getWeapon(ItemSlot.KNIFE);
        Team team = game.getGameMode().getTeam(gamePlayer);

        event.setCancelled(team != null && team == game.getGameMode().getTeam(damager)
                || !(damager.getLoadout().getWeapon(damager.getPlayer().getInventory().getItemInMainHand()) instanceof Knife));
        event.setDamage(0.0);

        knife.damage(gamePlayer);
    }

    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null) {
            return;
        }

        event.setDeathMessage(null);
        event.setKeepInventory(true);

        DeathCause deathCause = DeathCause.fromDamageCause(player.getLastDamageCause().getCause());

        if (deathCause == null) {
            return; // Only notify the game of death events the game should handle
        }

        plugin.getServer().getPluginManager().callEvent(new GamePlayerDeathEvent(game, game.getPlayerManager().getGamePlayer(player), deathCause));
    }

    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null || game.getArena() == null || !game.getState().isAllowItems()) {
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Item item = game.getItemRegistry().getItemIgnoreMetadata(player.getInventory().getItemInMainHand());

        if (item == null || item instanceof Weapon && ((Weapon) item).getGamePlayer() != gamePlayer) {
            return;
        }

        event.setCancelled(true);
        game.getItemRegistry().interact(item, event.getAction());
    }

    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (event.getFrom().getX() == event.getTo().getX() && event.getFrom().getZ() == event.getTo().getZ() || game == null || game.getArena() == null || game.getState().isAllowMove()) {
            return;
        }

        Arena arena = game.getArena();

        if (!arena.contains(player.getLocation())) {
            player.teleport(player.getLocation().add(event.getFrom().toVector().subtract(event.getTo().toVector()).normalize()));
        }

        Location location = game.getArena().getSpawn(game.getPlayerManager().getGamePlayer(player)).getLocation();
        location.setPitch(player.getLocation().getPitch());
        location.setYaw(player.getLocation().getYaw());
        player.teleport(location);
    }

    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null || !game.getState().isInProgress()) {
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);

        game.getPlayerManager().changeLoadout(gamePlayer, gamePlayer.getLoadout(), true);
        event.setRespawnLocation(game.getGameMode().getRespawnPoint(gamePlayer).getLocation());
    }
}
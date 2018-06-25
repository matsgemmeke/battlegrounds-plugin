package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.event.GamePlayerKillPlayerEvent;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GameEventHandler implements Listener {

    private Battlegrounds plugin;
    private Game game;

    public GameEventHandler(Battlegrounds plugin, Game game) {
        this.plugin = plugin;
        this.game = game;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private Item getDroppedItem(ItemStack itemStack) {
        for (Item item : game.getItemRegistry().getItems()) {
            if (item instanceof Droppable && ((Droppable) item).isRelated(itemStack)) {
                return item;
            }
        }
        return null;
    }

    private Item getInteractItem(GamePlayer gamePlayer, ItemStack itemStack) {
        Item item = game.getItemRegistry().getWeaponIgnoreMetadata(gamePlayer, itemStack);
        if (item == null && (item = game.getItemRegistry().getItemIgnoreMetadata(itemStack)) == null) {
            return null;
        }
        return item;
    }

    private boolean isPlaying(Player player) {
        Game game = plugin.getGameManager().getGame(player);
        return game != null && game == this.game;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!isPlaying(player)) {
            for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                event.getRecipients().remove(gamePlayer.getPlayer());
            }
            return;
        }

        event.setCancelled(true);
        game.getPlayerManager().receivePlayerChat(player, event.getMessage());

        if (plugin.getBattlegroundsConfig().broadcastChat) {
            plugin.getLogger().info("[Game " + game.getId() + "] " + player.getName() + ": " + event.getMessage());
        }
    }

    @EventHandler
    public void onCommandSend(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        List<String> list = plugin.getBattlegroundsConfig().allowedCommands;

        if (!isPlaying(player) || list.contains("*") || list.contains(event.getMessage().split(" ")[0].substring(1, event.getMessage().split(" ")[0].length()))) {
            return;
        }

        event.setCancelled(true);

        EnumMessage.COMMAND_NOT_ALLOWED.send(player);
    }

    @EventHandler
    public void onGamePlayerDeath(GamePlayerDeathEvent event) {
        event.getGame().broadcastMessage(Placeholder.replace(event.getDeathCause().getDeathMessage(), new Placeholder("bg_player", event.getGamePlayer().getName())));
    }

    @EventHandler
    public void onGamePlayerKill(GamePlayerKillPlayerEvent event) {
        event.getGame().getGameMode().onKill(event.getGamePlayer(), event.getKiller(), event.getWeapon(), event.getHitbox());
    }

    @EventHandler
    public void onItemSwitch(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (!isPlaying(player) || game == null || !game.getState().isInProgress()) {
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Weapon weapon = gamePlayer.getLoadout().getWeapon(player.getInventory().getItemInMainHand());

        if (weapon == null) {
            return;
        }

        weapon.onSwitch(player);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        event.setCancelled(isPlaying((Player) event.getEntity()) && !game.getState().isInProgress());
    }

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        boolean differentGame = plugin.getGameManager().getGame((Player) event.getDamager()) != game;

        if (!isPlaying(player) || game == null || differentGame) {
            event.setCancelled(differentGame);
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player), damager = game.getPlayerManager().getGamePlayer((Player) event.getDamager());
        Team team = game.getGameMode().getTeam(gamePlayer);

        if (gamePlayer == null
                || damager == null
                || damager.getLoadout() == null
                || !(damager.getLoadout().getWeapon(damager.getPlayer().getInventory().getItemInMainHand()) instanceof Knife)
                || team == game.getGameMode().getTeam(damager)) {
            event.setCancelled(true);
            return;
        }

        ((Knife) damager.getLoadout().getWeapon(ItemSlot.KNIFE)).damage(gamePlayer);

        event.setDamage(0.0);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (!isPlaying(player) || game == null) {
            return;
        }

        event.setDeathMessage(null);

        DeathCause deathCause = DeathCause.fromDamageCause(player.getLastDamageCause().getCause());

        if (deathCause == null) {
            return; // Only notify the game of death events the game should handle
        }

        plugin.getServer().getPluginManager().callEvent(new GamePlayerDeathEvent(game, game.getPlayerManager().getGamePlayer(player), deathCause));
    }

    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(isPlaying((Player) event.getEntity()));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!isPlaying(player) || game == null || game.getArena() == null || !game.getState().isAllowItems()) {
            return;
        }

        event.setCancelled(true);

        Item item = getInteractItem(game.getPlayerManager().getGamePlayer(player), player.getInventory().getItemInMainHand());

        if (item == null || !game.getState().isAllowWeapons() && item instanceof Weapon) {
            return;
        }

        game.getItemRegistry().interact(player, item, event.getAction());
    }

    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Item item = getInteractItem(game.getPlayerManager().getGamePlayer(player), event.getItemDrop().getItemStack());

        if (item == null || !(item instanceof Droppable)) {
            event.setCancelled(true);
            return;
        }

        ((Droppable) item).onDrop(player);
    }

    @EventHandler
    public void onPlayerItemPickUp(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Item item = getDroppedItem(event.getItem().getItemStack());

        if (item == null || !(item instanceof Droppable)) {
            event.setCancelled(true);
            return;
        }

        ((Droppable) item).onPickUp((Player) event.getEntity(), event.getItem());
    }

    @EventHandler
    public void onPlayerItemSwap(PlayerSwapHandItemsEvent event) {
        event.setCancelled(isPlaying(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!isPlaying(player) || game == null) {
            return;
        }

        game.getPlayerManager().onPlayerMove(player, event.getFrom(), event.getTo());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        if (!isPlaying(player) || game == null || !game.getState().isInProgress()) {
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Spawn spawn = game.getGameMode().getRespawnPoint(gamePlayer);

        game.getPlayerManager().respawnPlayer(gamePlayer, spawn);
        event.setRespawnLocation(spawn.getLocation());
    }
}
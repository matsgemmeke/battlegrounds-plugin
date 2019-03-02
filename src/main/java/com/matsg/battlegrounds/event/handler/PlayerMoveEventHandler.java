package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.handler.EventHandler;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.util.ActionBar;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveEventHandler implements EventHandler<PlayerMoveEvent> {

    private Battlegrounds plugin;

    public PlayerMoveEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null) {
            return;
        }

        Arena arena = game.getArena();
        Location from = event.getFrom(), to = event.getTo();

        if (game.getState().isInProgress() && arena != null && arena.hasBorders() && !arena.contains(to)) {
            player.teleport(from.add(from.toVector().subtract(to.toVector()).normalize()));
            ActionBar.LEAVE_ARENA.send(player);
        }

        if (arena == null || game.getState().isAllowMove() || from.getX() == to.getX() && from.getZ() == to.getZ()) {
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Spawn spawn = arena.getSpawn(gamePlayer);

        if (spawn == null && (spawn = arena.getTeamBase(gamePlayer.getTeam().getId())) == null) {
            return;
        }

        Location location = spawn.getLocation();
        location.setPitch(from.getPitch());
        location.setYaw(from.getYaw());
        player.teleport(location);
    }
}

package com.matsg.battlegrounds.event.handler;

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

    private Game game;

    public PlayerMoveEventHandler(Game game) {
        this.game = game;
    }

    public boolean handle(PlayerMoveEvent event) {
        Arena arena = game.getArena();
        Location from = event.getFrom(), to = event.getTo();
        Player player = event.getPlayer();

        if (arena != null && !arena.contains(player.getLocation())) {
            player.teleport(player.getLocation().add(from.toVector().subtract(to.toVector()).normalize()));
            ActionBar.LEAVE_ARENA.send(player);
        }

        if (arena == null || game.getState().isAllowMove() || from.getX() == to.getX() && from.getZ() == to.getZ()) {
            return event.isCancelled();
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Spawn spawn = arena.getSpawn(gamePlayer) != null ? arena.getSpawn(gamePlayer) : arena.getTeamBase(game.getGameMode().getTeam(gamePlayer));

        if (spawn == null) {
            return event.isCancelled();
        }

        Location location = spawn.getLocation();
        location.setPitch(player.getLocation().getPitch());
        location.setYaw(player.getLocation().getYaw());
        player.teleport(location);
        return event.isCancelled();
    }
}
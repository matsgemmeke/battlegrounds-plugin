package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameAction;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveEventHandler implements EventHandler<PlayerMoveEvent> {

    private Battlegrounds plugin;
    private InternalsProvider internals;
    private Translator translator;

    public PlayerMoveEventHandler(Battlegrounds plugin, InternalsProvider internals, Translator translator) {
        this.plugin = plugin;
        this.internals = internals;
        this.translator = translator;
    }

    public void handle(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);
        Location from = event.getFrom(), to = event.getTo();

        if (game == null || game.getArena() == null || to == null) {
            return;
        }

        Arena arena = game.getArena();
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);

        // If the player has a down state, do not allow the player to move
        if (gamePlayer.getDownState() != null && hasChangedPosition(from, to)) {
            Location returnLocation = gamePlayer.getDownState().getLocation().clone();
            returnLocation.setPitch(player.getLocation().getPitch());
            returnLocation.setYaw(player.getLocation().getYaw());

            player.teleport(returnLocation);
            return;
        }

        // Check if the player has not moved on the x or z axis
        if (from.getX() == to.getX() && from.getZ() == to.getZ()) {
            return;
        }

        // If the game is in progress and the player tries to leave the arena, send them back
        if (game.getState().isInProgress() && arena.hasBorders() && !arena.contains(to)) {
            player.teleport(from.add(from.toVector().subtract(to.toVector()).normalize()));

            String actionBar = translator.translate(TranslationKey.ACTIONBAR_LEAVE_ARENA.getPath());
            internals.sendActionBar(player, actionBar);
            return;
        }

        Spawn spawn = arena.getSpawn(gamePlayer);

        // Don't return players to their spawns if the game has started
        if (game.getState().isAllowed(GameAction.MOVEMENT)) {
            return;
        }

        if (spawn == null && (spawn = arena.getTeamBase(gamePlayer.getTeam().getId())) == null) {
            return;
        }

        Location location = spawn.getLocation();
        location.setPitch(from.getPitch());
        location.setYaw(from.getYaw());
        player.teleport(location);
    }

    private boolean hasChangedPosition(Location from, Location to) {
        return from.getX() != to.getX()
                || from.getY() != to.getY()
                || from.getZ() != to.getZ();
    }
}

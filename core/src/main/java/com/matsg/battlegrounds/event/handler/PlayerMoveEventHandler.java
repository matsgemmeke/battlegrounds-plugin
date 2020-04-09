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

        if (game == null) {
            return;
        }

        Arena arena = game.getArena();
        Location from = event.getFrom(), to = event.getTo();

        // Check if there is an arena and whether the player has moved on the x or z axis
        if (arena == null || game.getState().isAllowed(GameAction.MOVEMENT) || from.getX() == to.getX() && from.getZ() == to.getZ()) {
            return;
        }

        // If the game is in progress and the player tries to leave the arena, send them back
        if (game.getState().isInProgress()) {
            if (arena.hasBorders() && !arena.contains(to)) {
                player.teleport(from.add(from.toVector().subtract(to.toVector()).normalize()));

                String actionBar = translator.translate(TranslationKey.ACTIONBAR_LEAVE_ARENA.getPath());
                internals.sendActionBar(player, actionBar);
            }
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Spawn spawn = arena.getSpawn(gamePlayer);

        // If the player's state does not allow the player to move, send them back
        if (!gamePlayer.getState().canMove()) {
            player.teleport(from);
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
}

package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.Action;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
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

        if (game.getState().isInProgress() && arena != null && arena.hasBorders() && !arena.contains(to)) {
            player.teleport(from.add(from.toVector().subtract(to.toVector()).normalize()));

            String actionBar = translator.translate(TranslationKey.ACTIONBAR_LEAVE_ARENA.getPath());
            internals.sendActionBar(player, actionBar);
        }

        if (arena == null || game.getState().isAllowed(Action.MOVEMENT) || from.getX() == to.getX() && from.getZ() == to.getZ()) {
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

package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.ArenaComponent;
import com.matsg.battlegrounds.api.game.ComponentWrapper;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Interactable;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;

public class ComponentInteractHandler implements EventHandler<PlayerInteractEvent> {

    private Battlegrounds plugin;

    public ComponentInteractHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null || !game.getState().isInProgress()) {
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);

        boolean canInteract = Arrays.stream(gamePlayer.getLoadout().getWeapons()).noneMatch(w -> w != null && w.isInUse());
        boolean interactionHappened = false;

        if (!canInteract) {
            return;
        }

        for (ComponentWrapper componentWrapper : game.getComponentWrappers()) {
            ArenaComponent component = componentWrapper.getComponent(block.getLocation());

            // Perhaps look for a way this cast can be replaced
            if (component instanceof Interactable) {
                interactionHappened = interactionHappened || ((Interactable) component).onInteract(gamePlayer, block);
            }
        }

        if (interactionHappened) {
            game.updateScoreboard(); // Only update the scoreboard if an interaction has taken place
        }

        event.setCancelled(interactionHappened);
    }
}

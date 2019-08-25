package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.ArenaComponent;
import com.matsg.battlegrounds.api.game.ComponentWrapper;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Watchable;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Set;

public class ComponentMoveHandler implements EventHandler<PlayerMoveEvent> {

    private Battlegrounds plugin;

    public ComponentMoveHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null) {
            return;
        }

        Block block = player.getTargetBlock((Set<Material>) null, 2);
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);

        if (!gamePlayer.getState().canInteract()) {
            return;
        }

        for (ComponentWrapper componentWrapper : game.getComponentWrappers()) {
            ArenaComponent component = componentWrapper.getComponent(block.getLocation());

            // Perhaps look for a way this cast can be replaced
            if (component instanceof Watchable) {
                ((Watchable) component).onLook(gamePlayer, block);
            }
        }
    }
}

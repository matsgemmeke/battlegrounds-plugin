package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.handler.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Droppable;
import com.matsg.battlegrounds.api.item.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickupItemEventHandler implements EventHandler<PlayerPickupItemEvent> {

    private Battlegrounds plugin;

    public PlayerPickupItemEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null) {
            return;
        }

        Item item = null;

        for (Item i : game.getItemRegistry().getItems()) {
            if (i instanceof Droppable && ((Droppable) i).isRelated(event.getItem().getItemStack())) {
                item = i;
                break;
            }
        }

        if (item == null) {
            event.setCancelled(true);
            return;
        }

        event.setCancelled(((Droppable) item).onPickUp(game.getPlayerManager().getGamePlayer(player), event.getItem()));
    }
}

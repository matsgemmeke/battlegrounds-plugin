package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.event.handler.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Droppable;
import com.matsg.battlegrounds.api.item.Item;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickupItemEventHandler implements EventHandler<PlayerPickupItemEvent> {

    private Game game;

    public PlayerPickupItemEventHandler(Game game) {
        this.game = game;
    }

    public boolean handle(PlayerPickupItemEvent event) {
        Item item = null;

        for (Item i : game.getItemRegistry().getItems()) {
            if (i instanceof Droppable && ((Droppable) i).isRelated(event.getItem().getItemStack())) {
                item = i;
                break;
            }
        }

        if (item == null) {
            return event.isCancelled();
        }

        ((Droppable) item).onPickUp(event.getPlayer(), event.getItem());
        return true;
    }
}
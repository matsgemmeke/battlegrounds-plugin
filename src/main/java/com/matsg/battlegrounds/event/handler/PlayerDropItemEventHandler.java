package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.event.handler.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.DropListener;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDropItemEventHandler implements EventHandler<PlayerDropItemEvent> {

    private Game game;

    public PlayerDropItemEventHandler(Game game) {
        this.game = game;
    }

    public boolean handle(PlayerDropItemEvent event) {
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(event.getPlayer());
        ItemStack itemStack = event.getItemDrop().getItemStack();
        Item item = game.getItemRegistry().getWeaponIgnoreMetadata(gamePlayer, itemStack);

        if (item == null && (item = game.getItemRegistry().getItemIgnoreMetadata(itemStack)) == null || !game.getState().isAllowItems() || !(item instanceof DropListener)) {
            return true;
        }

        return ((DropListener) item).onDrop();
    }
}
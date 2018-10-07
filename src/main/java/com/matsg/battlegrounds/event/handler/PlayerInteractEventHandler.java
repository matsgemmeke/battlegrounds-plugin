package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.event.handler.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractEventHandler implements EventHandler<PlayerInteractEvent> {

    private Game game;

    public PlayerInteractEventHandler(Game game) {
        this.game = game;
    }

    public boolean handle(PlayerInteractEvent event) {
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(event.getPlayer());
        ItemStack itemStack = event.getItem();

        if (itemStack == null || game.getArena() == null || !game.getState().isAllowItems()) {
            return event.isCancelled();
        }

        event.setCancelled(true);

        Item item = game.getItemRegistry().getWeaponIgnoreMetadata(gamePlayer, itemStack);

        if (item == null && (item = game.getItemRegistry().getItemIgnoreMetadata(itemStack)) == null || !game.getState().isAllowWeapons() && item instanceof Weapon) {
            return event.isCancelled();
        }

        game.getItemRegistry().interact(gamePlayer.getPlayer(), item, event.getAction());
        return event.isCancelled();
    }
}
package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.Action;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractEventHandler implements EventHandler<PlayerInteractEvent> {

    private Battlegrounds plugin;

    public PlayerInteractEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(PlayerInteractEvent event) {
        handleItemInteraction(event);
        handleSignInteraction(event);
    }

    public void handleItemInteraction(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null) {
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        ItemStack itemStack = event.getItem();

        if (itemStack == null || game.getArena() == null || !game.getState().isAllowed(Action.USE_ITEM)) {
            return;
        }

        event.setCancelled(true);

        Item item = game.getItemRegistry().getWeapon(gamePlayer, itemStack);

        if (item == null && (item = game.getItemRegistry().getItem(itemStack)) == null || !game.getState().isAllowed(Action.USE_WEAPON) && item instanceof Weapon) {
            return;
        }

        game.getItemRegistry().interact(gamePlayer, item, event.getAction());
    }

    public void handleSignInteraction(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || !(event.getClickedBlock().getState() instanceof Sign)) {
            return;
        }

        Sign sign = (Sign) event.getClickedBlock().getState();

        for (Game i : plugin.getGameManager().getGames()) {
            if (i.getGameSign() != null && i.getGameSign().getSign().equals(sign)) {
                i.getGameSign().click(event.getPlayer());
                break;
            }
        }
    }
}

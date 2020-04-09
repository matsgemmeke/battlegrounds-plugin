package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameAction;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class PlayerItemInteractHandler implements EventHandler<PlayerInteractEvent> {

    private Battlegrounds plugin;

    public PlayerItemInteractHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);
        ItemStack itemStack = event.getItem();

        if (game == null || itemStack == null || !game.getState().isAllowed(GameAction.USE_ITEM)) {
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Item item = game.getItemRegistry().getWeaponIgnoreMetadata(gamePlayer, itemStack);

        if (item == null && (item = game.getItemRegistry().getItemIgnoreMetadata(itemStack)) == null || !game.getState().isAllowed(GameAction.USE_WEAPON) && item instanceof Weapon) {
            return;
        }

        Action action = event.getAction();
        BiConsumer<GamePlayer, PlayerInteractEvent> clickFunction;

        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            clickFunction = item::onLeftClick;
        } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            clickFunction = item::onRightClick;
        } else {
            return;
        }

        clickFunction.accept(gamePlayer, event);
    }
}

package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.util.EnumTitle;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntityEventHandler implements EventHandler<PlayerInteractEntityEvent> {

    private Battlegrounds plugin;

    public PlayerInteractEntityEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null || !(event.getRightClicked() instanceof Player)) {
            return;
        }

        GamePlayer reviver = game.getPlayerManager().getGamePlayer(player);
        GamePlayer downedPlayer = game.getPlayerManager().getGamePlayer((Player) event.getRightClicked());

        if (!game.getState().isInProgress()
                || downedPlayer == null
                || downedPlayer.getState().canMove()
                || downedPlayer.getDownState() == null
                || downedPlayer.getDownState().getReviver() != null
                || reviver.getLocation().distance(downedPlayer.getLocation()) > downedPlayer.getDownState().getMaxRevivingDistance()) {
            return;
        }

        downedPlayer.getDownState().setReviver(reviver);

        EnumTitle.PLAYER_BEING_REVIVED.send(downedPlayer.getPlayer(), new Placeholder("player_name", reviver.getName()));
        EnumTitle.PLAYER_REVIVING_PLAYER.send(reviver.getPlayer(), new Placeholder("player_name", downedPlayer.getName()));
    }
}

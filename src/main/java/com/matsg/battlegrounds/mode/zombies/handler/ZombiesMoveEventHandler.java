package com.matsg.battlegrounds.mode.zombies.handler;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Set;

public class ZombiesMoveEventHandler implements EventHandler<PlayerMoveEvent> {

    private Game game;
    private Zombies zombies;

    public ZombiesMoveEventHandler(Game game, Zombies zombies) {
        this.game = game;
        this.zombies = zombies;
    }

    public void handle(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);

        if (gamePlayer == null) {
            return;
        }

        Block block = player.getTargetBlock((Set<Material>) null, 2);

        if (!gamePlayer.getState().canInteract()) {
            return;
        }

        for (Section section : zombies.getSectionContainer().getAll()) {
            ArenaComponent component = section.getComponent(block.getLocation());

            // Perhaps look for a way this cast can be replaced
            if (component instanceof Watchable) {
                ((Watchable) component).onLook(gamePlayer, block);
            }
        }
    }
}

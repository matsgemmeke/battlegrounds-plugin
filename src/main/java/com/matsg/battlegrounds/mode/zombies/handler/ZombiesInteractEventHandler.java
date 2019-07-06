package com.matsg.battlegrounds.mode.zombies.handler;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.ArenaComponent;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Interactable;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;

public class ZombiesInteractEventHandler implements EventHandler<PlayerInteractEvent> {

    private Game game;
    private Zombies zombies;

    public ZombiesInteractEventHandler(Game game, Zombies zombies) {
        this.game = game;
        this.zombies = zombies;
    }

    public void handle(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);

        if (gamePlayer == null) {
            return;
        }

        Block block = player.getTargetBlock((Set<Material>) null, 5);

        boolean eventCancelled = false;

        for (Section section : zombies.getSectionContainer().getAll()) {
            ArenaComponent component = section.getComponent(block.getLocation());

            // Perhaps look for a way this cast can be replaced
            if (component instanceof Interactable) {
                eventCancelled = eventCancelled || ((Interactable) component).onInteract(gamePlayer, block);
            }
        }

        if (eventCancelled) {
            game.updateScoreboard(); // Only update the scoreboard if an interaction has taken place
        }

        event.setCancelled(eventCancelled);
    }
}

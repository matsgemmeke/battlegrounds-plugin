package com.matsg.battlegrounds.mode.zombies.handler;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.component.Barricade;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class BarricadePlayerPassHandler implements EventHandler<PlayerMoveEvent> {

    private Game game;
    private Zombies zombies;

    public BarricadePlayerPassHandler(Game game, Zombies zombies) {
        this.game = game;
        this.zombies = zombies;
    }

    public void handle(PlayerMoveEvent event) {
        if (!zombies.isActive()) {
            return;
        }

        Player player = event.getPlayer();
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Location from = event.getFrom(), to = event.getTo();

        if (gamePlayer == null || to == null) {
            return;
        }

        for (Section section : zombies.getSectionContainer().getAll()) {
            for (Barricade barricade : section.getBarricadeContainer().getAll()) {
                // If the pass interaction was accepted, break the loop to execute less math calculations
                if (barricade != null && barricade.onPass(gamePlayer, from, to)) {
                    break;
                }
            }
        }
    }
}

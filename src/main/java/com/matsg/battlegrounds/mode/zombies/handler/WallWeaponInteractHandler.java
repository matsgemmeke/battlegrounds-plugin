package com.matsg.battlegrounds.mode.zombies.handler;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import com.matsg.battlegrounds.mode.zombies.component.WallWeapon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class WallWeaponInteractHandler implements EventHandler<PlayerInteractEntityEvent> {

    private Game game;
    private Zombies zombies;

    public WallWeaponInteractHandler(Game game, Zombies zombies) {
        this.game = game;
        this.zombies = zombies;
    }

    public void handle(PlayerInteractEntityEvent event) {
        if (!zombies.isActive()) {
            return;
        }

        Player player = event.getPlayer();
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);

        if (gamePlayer == null) {
            return;
        }

        Entity entity = event.getRightClicked();

        for (Section section : zombies.getSectionContainer().getAll()) {
            for (WallWeapon wallWeapon : section.getWallWeaponContainer().getAll()) {
                // The entity instances may differ over time so compare the locations instead
                if (wallWeapon.getItemFrame().getLocation().equals(entity.getLocation())) {
                    // Initiate an interaction with the wall weapon
                    wallWeapon.onInteract(gamePlayer, entity.getLocation().getBlock());
                    // Cancel the event so the item frame can not be tampered with
                    event.setCancelled(true);
                }
            }
        }
    }
}

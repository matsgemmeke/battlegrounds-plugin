package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathEventHandler implements EventHandler<EntityDeathEvent> {

    private Battlegrounds plugin;

    public EntityDeathEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        Mob mob = null;

        for (Game game : plugin.getGameManager().getGames()) {
            if ((mob = game.getMobManager().findMob(entity)) != null) {
                break;
            }
        }

        if (mob == null) {
            return;
        }

        // Remove the drops upon the death of battlegrounds mobs
        event.getDrops().clear();
    }
}

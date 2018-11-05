package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.handler.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.Knife;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityEventHandler implements EventHandler<EntityDamageByEntityEvent> {

    private Battlegrounds plugin;

    public EntityDamageByEntityEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null) {
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer((Player) event.getEntity()), damager = game.getPlayerManager().getGamePlayer((Player) event.getDamager());
        Team team = gamePlayer.getTeam();

        if (damager.getLoadout() == null || team == damager.getTeam() || !(damager.getLoadout().getWeapon(damager.getPlayer().getItemInHand()) instanceof Knife)) {
            event.setCancelled(true);
            return;
        }

        event.setDamage(0.0);

        ((Knife) damager.getLoadout().getWeapon(ItemSlot.KNIFE)).damage(gamePlayer);
    }
}

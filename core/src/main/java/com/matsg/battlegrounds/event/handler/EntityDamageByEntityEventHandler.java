package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.MeleeWeapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDamageByEntityEventHandler implements EventHandler<EntityDamageByEntityEvent> {

    private Battlegrounds plugin;

    public EntityDamageByEntityEventHandler(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void handle(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();
        Game game = plugin.getGameManager().getGame(player);

        if (game == null) {
            return;
        }

        BattleEntity entity = game.getMobManager().findMob(event.getEntity());
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        ItemStack itemStack = player.getItemInHand();

        // In case the entity is not a mob, find the matching player instead
        if (entity == null && event.getEntity() instanceof Player) {
            entity = game.getPlayerManager().getGamePlayer((Player) event.getEntity());
        }

        if (entity == null
                || gamePlayer.getLoadout() == null
                || !entity.isHostileTowards(gamePlayer)
                || !(gamePlayer.getLoadout().getWeapon(itemStack) instanceof MeleeWeapon)) {
            event.setCancelled(true);
            return;
        }

        event.setDamage(0.0);

        ((MeleeWeapon) gamePlayer.getLoadout().getWeapon(itemStack)).damage(entity);
    }
}

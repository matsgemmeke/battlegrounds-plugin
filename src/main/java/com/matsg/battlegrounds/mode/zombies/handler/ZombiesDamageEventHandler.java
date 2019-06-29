package com.matsg.battlegrounds.mode.zombies.handler;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.event.GamePlayerDamageEntityEvent;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.Zombies;

public class ZombiesDamageEventHandler implements EventHandler<GamePlayerDamageEntityEvent> {

    private Zombies zombies;

    public ZombiesDamageEventHandler(Zombies zombies) {
        this.zombies = zombies;
    }

    public void handle(GamePlayerDamageEntityEvent event) {
        if (!zombies.isActive() || !(event.getEntity() instanceof Mob)) {
            return;
        }

        Game game = event.getGame();
        GamePlayer gamePlayer = event.getGamePlayer();
        Mob mob = (Mob) event.getEntity();

        double damage = zombies.getPowerUpManager().getPowerUpDamage(event.getDamage());
        int points = 10; // Points per hit constant

        mob.damage(damage);
        mob.getBukkitEntity().setCustomName(game.getMobManager().getHealthBar(mob));

        game.getPlayerManager().givePoints(gamePlayer, points);
        game.updateScoreboard();
    }
}

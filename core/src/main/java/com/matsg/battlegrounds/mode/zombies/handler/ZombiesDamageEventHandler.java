package com.matsg.battlegrounds.mode.zombies.handler;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.event.GamePlayerDamageEntityEvent;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.Zombies;

public class ZombiesDamageEventHandler implements EventHandler<GamePlayerDamageEntityEvent> {

    private InternalsProvider internals;
    private Translator translator;
    private Zombies zombies;

    public ZombiesDamageEventHandler(Zombies zombies, InternalsProvider internals, Translator translator) {
        this.zombies = zombies;
        this.internals = internals;
        this.translator = translator;
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

        gamePlayer.setPoints(gamePlayer.getPoints() + points);

        String actionBar = translator.translate(TranslationKey.ACTIONBAR_POINTS_INCREASE.getPath(), new Placeholder("bg_points", points));
        internals.sendActionBar(gamePlayer.getPlayer(), actionBar);

        game.updateScoreboard();
    }
}

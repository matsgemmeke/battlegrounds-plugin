package com.matsg.battlegrounds.mode.zombies.item.powerup;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpCallback;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpEffect;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class Nuke implements PowerUpEffect {

    private static final int NUKE_POINTS = 400;
    private Game game;
    private int duration;
    private InternalsProvider internals;
    private Material material;
    private String name;
    private TaskRunner taskRunner;
    private Translator translator;
    private Zombies zombies;

    public Nuke(Game game, Zombies zombies, String name, InternalsProvider internals, TaskRunner taskRunner, Translator translator) {
        this.game = game;
        this.zombies = zombies;
        this.name = name;
        this.internals = internals;
        this.taskRunner = taskRunner;
        this.translator = translator;
        this.duration = 0;
        this.material = Material.TNT;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public void activate(PowerUpCallback callback) {
        taskRunner.runTaskTimer(new BukkitRunnable() {
            public void run() {
                if (!game.getState().isInProgress()) {
                    cancel();
                    return;
                }

                if (game.getMobManager().getMobs().size() <= 0 || !game.getState().isInProgress()) {
                    for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                        String actionBar = translator.translate(TranslationKey.ACTIONBAR_POINTS_INCREASE.getPath(), new Placeholder("bg_points", NUKE_POINTS));
                        internals.sendActionBar(gamePlayer.getPlayer(), actionBar);

                        gamePlayer.setPoints(gamePlayer.getPoints() + NUKE_POINTS);
                    }

                    game.updateScoreboard();

                    if (!zombies.getWave().isRunning()) {
                        zombies.startWave(zombies.getWave().getRound() + 1);
                    }

                    callback.onPowerUpEnd();
                    cancel();
                    return;
                }

                Mob mob = game.getMobManager().getMobs().get(0);
                mob.getBukkitEntity().getWorld().createExplosion(mob.getBukkitEntity().getLocation().add(0, 0.5, 0), 0);
                mob.remove();

                game.getMobManager().getMobs().remove(mob);
                game.updateScoreboard();
            }
        }, 20, 8);
    }

    public boolean isApplicableForActivation() {
        return game.getMobManager().getMobs().size() > 0;
    }

    public double modifyDamage(double damage) {
        return damage;
    }

    public int modifyPoints(int points) {
        return points;
    }
}

package com.matsg.battlegrounds.mode.zombies.item.powerup;

import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpCallback;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpEffect;
import com.matsg.battlegrounds.util.BattleRunnable;
import org.bukkit.Material;

public class Nuke implements PowerUpEffect {

    private static final int NUKE_POINTS = 400;
    private int duration;
    private Material material;
    private String name;
    private Zombies zombies;

    public Nuke(Zombies zombies, String name) {
        this.zombies = zombies;
        this.name = name;
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

    public void activate(Game game, PowerUpCallback callback) {
        new BattleRunnable() {
            public void run() {
                if (!game.getState().isInProgress()) {
                    cancel();
                    return;
                }

                if (game.getMobManager().getMobs().size() <= 0 || !game.getState().isInProgress()) {
                    game.getPlayerManager().givePoints(NUKE_POINTS);
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
        }.runTaskTimer(20, 8);
    }

    public double modifyDamage(double damage) {
        return damage;
    }

    public int modifyPoints(int points) {
        return points;
    }
}

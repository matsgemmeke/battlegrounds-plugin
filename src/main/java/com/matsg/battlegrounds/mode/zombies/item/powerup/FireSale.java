package com.matsg.battlegrounds.mode.zombies.item.powerup;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBox;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpCallback;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpEffect;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class FireSale implements PowerUpEffect {

    private static final int MYSTERY_BOX_POINTS = 10;

    private Game game;
    private int duration;
    private Material material;
    private String name;
    private TaskRunner taskRunner;
    private Zombies zombies;

    public FireSale(Game game, Zombies zombies, String name, int duration, TaskRunner taskRunner) {
        this.game = game;
        this.zombies = zombies;
        this.name = name;
        this.duration = duration;
        this.taskRunner = taskRunner;
        this.material = Material.NAME_TAG;
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
        Collection<MysteryBox> mysteryBoxes = zombies.getComponents(MysteryBox.class);
        MysteryBox activeBox = zombies.getActiveMysteryBox();

        for (MysteryBox mysteryBox : mysteryBoxes) {
            mysteryBox.setActive(true);
            mysteryBox.setPrice(MYSTERY_BOX_POINTS);
        }

        taskRunner.runTaskLater(new BukkitRunnable() {
            public void run() {
                if (!game.getState().isInProgress()) {
                    return;
                }

                for (MysteryBox mysteryBox : mysteryBoxes) {
                    mysteryBox.setActive(mysteryBox == activeBox);
                    mysteryBox.setPrice(zombies.getConfig().getMysteryBoxPrice());
                }

                callback.onPowerUpEnd();
            }
        }, duration);
    }

    public boolean isApplicableForActivation() {
        return zombies.getComponents(MysteryBox.class).size() > 0 && zombies.getActiveMysteryBox() != null;
    }

    public double modifyDamage(double damage) {
        return damage;
    }

    public int modifyPoints(int points) {
        return points;
    }
}

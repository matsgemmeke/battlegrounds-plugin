package com.matsg.battlegrounds.mode.zombies.item.powerup;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBox;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpCallback;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpEffect;
import com.matsg.battlegrounds.util.BattleRunnable;
import org.bukkit.Material;

import java.util.Collection;

public class FireSale implements PowerUpEffect {

    private static final int MYSTERY_BOX_POINTS = 10;

    private Game game;
    private int duration;
    private Material material;
    private String name;
    private Zombies zombies;

    public FireSale(Game game, Zombies zombies, String name, int duration) {
        this.game = game;
        this.zombies = zombies;
        this.name = name;
        this.duration = duration;
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

        new BattleRunnable() {
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
        }.runTaskLater(duration);
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

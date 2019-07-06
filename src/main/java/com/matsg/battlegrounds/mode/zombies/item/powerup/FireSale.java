package com.matsg.battlegrounds.mode.zombies.item.powerup;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBox;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpCallback;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpEffect;
import com.matsg.battlegrounds.util.BattleRunnable;
import org.bukkit.Material;

public class FireSale implements PowerUpEffect {

    private static final int MYSTERY_BOX_POINTS = 10;
    private int duration;
    private Material material;
    private String name;
    private Zombies zombies;

    public FireSale(Zombies zombies, String name, int duration) {
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

    public void activate(Game game, PowerUpCallback callback) {
        MysteryBox activeBox = zombies.getActiveMysteryBox();

        if (activeBox == null) {
            return;
        }

        for (MysteryBox mysteryBox : game.getArena().getComponents(MysteryBox.class)) {
            mysteryBox.setActive(true);
            mysteryBox.setPrice(MYSTERY_BOX_POINTS);
        }

        new BattleRunnable() {
            public void run() {
                if (!game.getState().isInProgress()) {
                    return;
                }

                for (MysteryBox mysteryBox : game.getArena().getComponents(MysteryBox.class)) {
                    if (mysteryBox != activeBox) {
                        mysteryBox.setActive(false);
                    }

                    mysteryBox.setPrice(zombies.getConfig().getMysteryBoxPrice());
                }
            }
        }.runTaskLater(duration);
    }

    public double modifyDamage(double damage) {
        return damage;
    }

    public int modifyPoints(int points) {
        return points;
    }
}

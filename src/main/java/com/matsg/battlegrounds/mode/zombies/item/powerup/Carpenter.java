package com.matsg.battlegrounds.mode.zombies.item.powerup;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.MobSpawn;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpCallback;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpEffect;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Collection;

public class Carpenter implements PowerUpEffect {

    private static final int CARPENTER_POINTS = 200;
    private int duration;
    private Material material;
    private String name;

    public Carpenter(String name) {
        this.name = name;
        this.duration = 0;
        this.material = XMaterial.OAK_FENCE.parseMaterial();
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
        Collection<MobSpawn> mobSpawns = game.getArena().getComponents(MobSpawn.class);

        if (mobSpawns.size() <= 0) {
            return;
        }

        new BattleRunnable() {
            public void run() {
                if (!game.getState().isInProgress()) {
                    cancel();
                    return;
                }

                for (MobSpawn mobSpawn : mobSpawns) {
                    if (mobSpawn.getBarricade() != null && !mobSpawn.getBarricade().isClosed() && mobSpawn.getBarricade().getMobs().size() <= 0) {
                        for (Block block : mobSpawn.getBarricade().getBlocks()) {
                            if (block != null && block.getType() == Material.AIR) {
                                // Repair a single block and repeat the loop
                                mobSpawn.getBarricade().repairBlock(block);
                                block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType().getId());
                                return;
                            }
                        }
                    }
                }

                game.getPlayerManager().givePoints(CARPENTER_POINTS);
                game.updateScoreboard();
                callback.onPowerUpEnd();
                cancel();
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

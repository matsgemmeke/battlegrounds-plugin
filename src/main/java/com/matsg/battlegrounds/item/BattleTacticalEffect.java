package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.util.BattleRunnable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public enum BattleTacticalEffect implements TacticalEffect {

    BLINDNESS(1, true) {
        public void applyEffect(GamePlayer gamePlayer, Location location, int duration) {
            PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, duration, 1);
            gamePlayer.getPlayer().addPotionEffect(blindness);
        }

        public void onIgnite(Tactical tactical, Item item) {
            item.remove();
            tactical.getDroppedItems().remove(item);
        }
    },
    NOISE(2, false) {
        public void applyEffect(GamePlayer gamePlayer, Location location, int duration) { }

        private Gun getDefaultGun(Loadout loadout) {
            if (loadout.getPrimary() != null && loadout.getPrimary() instanceof Gun) {
                return (Gun) loadout.getPrimary();
            } else if (loadout.getSecondary() != null && loadout.getSecondary() instanceof Gun) {
                return (Gun) loadout.getSecondary();
            }
            return null;
        }

        public void onIgnite(Tactical tactical, Item item) {
            Loadout loadout = tactical.getGamePlayer().getLoadout();
            Gun gun = getDefaultGun(loadout);

            if (gun == null) {
                return;
            }

            long period = 2;

            new BattleRunnable() {
                int loops = 0, maxLoops = 40;
                public void run() {
                    loops += period;
                    for (Sound sound : gun.getShootSound()) {
                        sound.play(tactical.getGame(), item.getLocation());
                    }
                    if (loops > maxLoops) {
                        tactical.getDroppedItems().remove(item);
                        item.remove();
                        cancel();
                    }
                }
            }.runTaskTimer(0, period);
        }
    },
    SMOKE(3, false) {
        public void applyEffect(GamePlayer gamePlayer, Location location, int duration) { }

        private List<Block> getCircleBlocks(Location center, int radius, boolean hollow) {
            List<Block> circleBlocks = new ArrayList<>();
            int bx = center.getBlockX();
            int by = center.getBlockY();
            int bz = center.getBlockZ();

            for (int x = bx - radius; x <= bx + radius; x++) {
                for (int y = by - radius; y <= by + radius; y++) {
                    for (int z = bz - radius; z <= bz + radius; z++) {
                        double distance = ((bx-x) * (bx-x) + ((bz-z) * (bz-z)) + ((by-y) * (by-y)));
                        if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                            circleBlocks.add(new Location(center.getWorld(), x, y, z).getBlock());
                        }
                    }
                }
            }
            return circleBlocks;
        }

        public void onIgnite(Tactical tactical, Item item) {
            List<Block> blocks = getCircleBlocks(item.getLocation(), (int) tactical.getLongRange(), false);
            List<BlockState> blockStates = new ArrayList<>();

            tactical.getDroppedItems().remove(item);
            item.remove();

            for (Block block : blocks) {
                if (block.getType() == Material.AIR) {
                    blockStates.add(block.getState());
                    block.setType(Material.BEETROOT_BLOCK);
                }
            }

            new BattleRunnable() {
                public void run() {
                    for (BlockState blockState : blockStates) {
                        blockState.update(true, false);
                    }
                }
            }.runTaskLater(200);
        }
    };

    private boolean playerEffective;
    private int id;

    BattleTacticalEffect(int id, boolean playerEffective) {
        this.id = id;
        this.playerEffective = playerEffective;
    }

    public static TacticalEffect valueOf(int id) {
        for (BattleTacticalEffect tacticalEffect : values()) {
            if (tacticalEffect.id == id) {
                return tacticalEffect;
            }
        }
        throw new IllegalArgumentException();
    }

    public boolean isPlayerEffective() {
        return playerEffective;
    }

    public abstract void applyEffect(GamePlayer gamePlayer, Location location, int duration);

    public abstract void onIgnite(Tactical tactical, Item item);
}
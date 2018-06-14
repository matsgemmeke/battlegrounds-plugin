package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.item.Tactical;
import com.matsg.battlegrounds.api.item.TacticalEffect;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.util.Sound;
import org.bukkit.Location;
import org.bukkit.block.Block;
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

        public void onIgnite(Tactical tactical) {

        }
    },
    NOISE(2, false) {
        public void applyEffect(GamePlayer gamePlayer, Location location, int duration) {

        }

        public void onIgnite(Tactical tactical) {
            Loadout loadout = tactical.getGamePlayer().getLoadout();
            //Sound[] decoySound =
        }
    },
    SMOKE(3, false) {
        public void applyEffect(GamePlayer gamePlayer, Location location, int duration) {

        }

        private List<Block> getCircleBlocks(Location center, int radius, boolean hollow) {
            List<Block> circleBlocks = new ArrayList<Block>();
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

        public void onIgnite(Tactical tactical) {

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

    public abstract void onIgnite(Tactical tactical);
}
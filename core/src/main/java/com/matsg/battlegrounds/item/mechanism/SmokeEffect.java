package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.entity.BattleEntityType;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.item.Tactical;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class SmokeEffect implements TacticalEffect {

    private Tactical tactical;
    private TaskRunner taskRunner;

    public SmokeEffect(TaskRunner taskRunner) {
        this.taskRunner = taskRunner;
    }

    public Tactical getWeapon() {
        return tactical;
    }

    public void setWeapon(Tactical tactical) {
        this.tactical = tactical;
    }

    public void applyEffect(Item item) {
        List<Block> blocks = getCircleBlocks(item.getLocation(), (int) tactical.getLongRange(), false);
        List<BlockState> blockStates = new ArrayList<>();

        tactical.getDroppedItems().remove(item);
        item.remove();

//        PotionEffect potionEffect = new PotionEffect(PotionEffectType.BLINDNESS, 100, 1);
//
//        for (BattleEntity entity : tactical.getContext().getNearbyEntities(item.getLocation(), tactical.getLongRange())) {
//            if (entity.getEntityType() == BattleEntityType.PLAYER) {
//                GamePlayer gamePlayer = (GamePlayer) entity;
//                gamePlayer.getPlayer().addPotionEffect(potionEffect);
//            }
//        }

        for (Block block : blocks) {
            if (block.getType() == Material.AIR) {
                blockStates.add(block.getState());
                block.setType(XMaterial.LEGACY_CROPS.parseMaterial());
            }
        }

        for (Sound sound : tactical.getIgnitionSound()) {
            sound.play(tactical.getGame(), item.getLocation());
        }

        taskRunner.runTaskLater(new BukkitRunnable() {
            public void run() {
                for (BlockState blockState : blockStates) {
                    blockState.update(true, false);
                }
            }
        }, 200);
    }

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
}

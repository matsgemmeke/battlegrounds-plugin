package com.matsg.battlegrounds.game.mode.zombies;

import com.matsg.battlegrounds.api.entity.Mob;
import org.bukkit.entity.Entity;

import java.util.Set;

public class MobManager {

    private Set<Mob> mobs;

    public Set<Mob> getMobs() {
        return mobs;
    }

    public Mob findMob(Entity entity) {
        for (Mob mob : mobs) {
            if (mob.getBukkitEntity() == entity) {
                return mob;
            }
        }
        return null;
    }
}

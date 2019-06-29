package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.Mob;
import org.bukkit.entity.Entity;

import java.util.List;

public interface MobManager {

    List<Mob> getMobs();

    Mob findMob(Entity entity);

    String getHealthBar(Mob mob);
}

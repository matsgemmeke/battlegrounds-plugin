package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.MobManager;
import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.storage.BattlegroundsConfig;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class BattleMobManager implements MobManager {

    private BattlegroundsConfig config;
    private List<Mob> mobs;

    public BattleMobManager(BattlegroundsConfig config) {
        this.config = config;
        this.mobs = new ArrayList<>();
    }

    public List<Mob> getMobs() {
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

    public String getHealthBar(Mob mob) {
        String healthBarSymbol = config.mobHealthBarSymbol;
        StringBuilder builder = new StringBuilder(ChatColor.translateAlternateColorCodes('&', config.mobHealthBarStartSymbol) + ChatColor.DARK_RED);
        int length = (int) (mob.getHealth() / (mob.getMaxHealth() / config.mobHealthBarLength));

        for (int i = 1; i <= length; i++) {
            builder.append(healthBarSymbol);
        }

        builder.append(ChatColor.GRAY);

        for (int j = 1; j <= config.mobHealthBarLength - length; j++) {
            builder.append(healthBarSymbol);
        }

        builder.append(ChatColor.translateAlternateColorCodes('&', config.mobHealthBarEndSymbol));
        return builder.toString();
    }
}

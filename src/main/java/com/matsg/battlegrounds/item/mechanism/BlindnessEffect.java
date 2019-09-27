package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.entity.BattleEntityType;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Tactical;
import com.matsg.battlegrounds.api.item.TacticalEffect;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BlindnessEffect implements TacticalEffect {

    private int duration;
    private Tactical tactical;

    public BlindnessEffect(int duration) {
        this.duration = duration;
    }

    public Tactical getWeapon() {
        return tactical;
    }

    public void setWeapon(Tactical tactical) {
        this.tactical = tactical;
    }

    public void applyEffect(Location location) {
        double range = tactical.getLongRange();
        PotionEffect potionEffect = new PotionEffect(PotionEffectType.BLINDNESS, duration, 1);
        Team team = tactical.getGamePlayer().getTeam();

        for (BattleEntity entity : tactical.getContext().getNearbyEntities(location, team, range)) {
            if (entity.getEntityType() == BattleEntityType.PLAYER) {
                GamePlayer gamePlayer = (GamePlayer) entity;

                gamePlayer.getPlayer().addPotionEffect(potionEffect);
            }
        }
    }

    public void igniteItem(Item item) {
        item.remove();
        tactical.getDroppedItems().remove(item);
    }
}

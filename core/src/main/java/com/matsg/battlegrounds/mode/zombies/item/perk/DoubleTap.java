package com.matsg.battlegrounds.mode.zombies.item.perk;

import com.matsg.battlegrounds.api.item.DamageSource;
import com.matsg.battlegrounds.api.item.Firearm;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffect;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffectType;
import org.bukkit.Color;

public class DoubleTap extends PerkEffect {

    private static final double FIREARM_DAMAGE_BUFF = 1.33;
    private static final double FIREARM_DAMAGE_NERF = 0.67;
    private static final int MAX_NUMBER_OF_FIREARMS = 2;

    private Firearm[] firearms;

    public DoubleTap(String displayName) {
        super(PerkEffectType.DOUBLE_TAP, displayName, Color.fromRGB(250, 125, 0));
        this.firearms = new Firearm[MAX_NUMBER_OF_FIREARMS];
    }

    public void apply() {
        firearms = gamePlayer.getLoadout().getFirearms();

        for (Firearm firearm : firearms) {
            if (firearm != null) {
                modifyDamageAttributes(firearm, FIREARM_DAMAGE_BUFF);
            }
        }
    }

    public void refresh() {
        for (int i = 0; i < firearms.length; i++) {
            Firearm firearm = gamePlayer.getLoadout().getFirearms()[i];
            // Check if the player had changed firearms and if so, apply the damage buff
            if (firearms[i] != firearm) {
                firearms[i] = firearm;
                modifyDamageAttributes(firearm, FIREARM_DAMAGE_BUFF);
            }
        }
    }

    public void removePerk() {
        for (Firearm firearm : firearms) {
            if (firearm != null) {
                modifyDamageAttributes(firearm, FIREARM_DAMAGE_NERF);
            }
        }
    }

    private void modifyDamageAttributes(Firearm firearm, double damage) {
        DamageSource projectile = firearm.getProjectile();
        projectile.setLongDamage(projectile.getLongDamage() * damage);
        projectile.setMidDamage(projectile.getMidDamage() * damage);
        projectile.setShortDamage(projectile.getShortDamage() * damage);
    }
}

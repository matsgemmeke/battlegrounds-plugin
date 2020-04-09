package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.item.ItemMetadata;
import com.matsg.battlegrounds.api.item.Tactical;
import com.matsg.battlegrounds.item.mechanism.TacticalEffect;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.item.mechanism.IgnitionSystem;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class BattleTactical extends BattleEquipment implements Tactical {

    private static final double TACTICAL_DAMAGE = 0.0;

    private int duration;
    private TacticalEffect effect;

    public BattleTactical(
            ItemMetadata metadata,
            ItemStack itemStack,
            InternalsProvider internals,
            TaskRunner taskRunner,
            IgnitionSystem ignitionSystem,
            Sound[] explodeSound,
            TacticalEffect effect,
            int amount,
            int maxAmount,
            int cooldown,
            int duration,
            int ignitionTime,
            double longRange,
            double midRange,
            double shortRange,
            double velocity
    ) {
        super(metadata, itemStack, internals, taskRunner, EquipmentType.TACTICAL, ignitionSystem, explodeSound, amount,
                maxAmount, cooldown, ignitionTime, longRange, midRange, shortRange, velocity);
        this.duration = duration;
        this.effect = effect;
    }

    public int getDuration() {
        return duration;
    }

    public TacticalEffect getEffect() {
        return effect;
    }

    public double getLongDamage() {
        return TACTICAL_DAMAGE;
    }

    public double getMidDamage() {
        return TACTICAL_DAMAGE;
    }

    public double getShortDamage() {
        return TACTICAL_DAMAGE;
    }

    public void setEffect(TacticalEffect effect) {
        this.effect = effect;
    }

    public BattleTactical clone() {
        BattleTactical tactical = (BattleTactical) super.clone();
        tactical.effect.setWeapon(tactical);
        return tactical;
    }

    public double getDamage(Hitbox hitbox, double distance) {
        return 0.0;
    }

    public void ignite(Item item) {
        effect.applyEffect(item);
    }

    public void setLongDamage(double longDamage) { }

    public void setMidDamage(double midDamage) { }

    public void setShortDamage(double shortDamage) { }
}

package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.item.ItemMetadata;
import com.matsg.battlegrounds.api.item.Tactical;
import com.matsg.battlegrounds.api.item.TacticalEffect;
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
            int cooldown,
            int ignitionTime,
            double longRange,
            double midRange,
            double shortRange,
            double velocity
    ) {
        super(metadata, itemStack, internals, taskRunner, EquipmentType.TACTICAL, ignitionSystem, explodeSound, amount,
                cooldown, ignitionTime, longRange, midRange, shortRange, velocity);
        this.effect = effect;
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

    public Tactical clone() {
        return (Tactical) super.clone();
    }

    public double getDamage(Hitbox hitbox, double distance) {
        return 0.0;
    }

    public void ignite(Item item) {
        effect.igniteItem(item);
        effect.applyEffect(item.getLocation());
    }

    public void setLongDamage(double longDamage) { }

    public void setMidDamage(double midDamage) { }

    public void setShortDamage(double shortDamage) { }
}

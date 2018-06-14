package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.Tactical;
import com.matsg.battlegrounds.api.item.TacticalEffect;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.player.Hitbox;
import com.matsg.battlegrounds.api.util.Sound;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class BattleTactical extends BattleEquipment implements Tactical {

    private int duration;
    private TacticalEffect effect;

    public BattleTactical(String name, String description, ItemStack itemStack, short durability,
                          int amount, TacticalEffect effect, int duration, double longRange, double midRange, double shortRange,
                          double velocity, int ignitionTime, Sound[] explodeSound) {
        super(name, description, itemStack, durability, EquipmentType.TACTICAL, amount, longRange, midRange, shortRange, velocity,
                ignitionTime == 0 ? IgnitionType.PASSIVE : IgnitionType.AGGRESSIVE, ignitionTime, explodeSound);
        this.duration = duration;
        this.effect = effect;
    }

    public Tactical clone() {
        return (Tactical) super.clone();
    }

    public int getDuration() {
        return duration;
    }

    public TacticalEffect getEffect() {
        return effect;
    }

    public double getLongDamage() {
        return 0.0;
    }

    public double getMidDamage() {
        return 0.0;
    }

    public double getShortDamage() {
        return 0.0;
    }

    public void setEffect(TacticalEffect effect) {
        this.effect = effect;
    }

    private void effectPlayers(Location location) {
        for (GamePlayer gamePlayer : game.getPlayerManager().getNearbyPlayers(location, longRange)) {
            if (gamePlayer != null && gamePlayer.getPlayer() != null && !gamePlayer.getPlayer().isDead() && gamePlayer.getStatus().isAlive()) {
                effect.applyEffect(gamePlayer, location, duration);
            }
        }
    }

    public double getDamage(Hitbox hitbox, double distance) {
        return 0.0;
    }

    public void ignite(Item item) {
        effect.onIgnite(this);
        effectPlayers(item.getLocation());
    }

    public void setLongDamage(double longDamage) { }

    public void setMidDamage(double midDamage) { }

    public void setShortDamage(double shortDamage) { }
}
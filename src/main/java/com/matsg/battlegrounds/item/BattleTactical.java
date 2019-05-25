package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.Tactical;
import com.matsg.battlegrounds.api.item.TacticalEffect;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.util.Sound;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class BattleTactical extends BattleEquipment implements Tactical {

    private static final double TACTICAL_DAMAGE = 0.0;

    private int duration;
    private TacticalEffect effect;

    public BattleTactical(Battlegrounds plugin, String id, String name, String description, ItemStack itemStack,
                          int amount, int cooldown, TacticalEffect effect, int duration, double longRange, double midRange, double shortRange,
                          double velocity, int ignitionTime, Sound[] explodeSound) {
        super(plugin, id, name, description, itemStack, EquipmentType.TACTICAL, amount, cooldown, longRange, midRange, shortRange, velocity,
                ignitionTime == 0 ? IgnitionType.PASSIVE : IgnitionType.AGGRESSIVE, ignitionTime, explodeSound);
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

    public Tactical clone() {
        return (Tactical) super.clone();
    }

    private void effectPlayers(Location location) {
        for (GamePlayer gamePlayer : game.getPlayerManager().getNearbyPlayers(location, longRange)) {
            if (gamePlayer != null && gamePlayer.getState().isAlive()
                    && !(gamePlayer != this.gamePlayer && game.getGameMode().getTeam(gamePlayer) == game.getGameMode().getTeam(this.gamePlayer))) {
                effect.applyEffect(gamePlayer, location, duration);
            }
        }
    }

    public double getDamage(Hitbox hitbox, double distance) {
        return 0.0;
    }

    public void ignite(Item item) {
        effect.onIgnite(this, item);
        effectPlayers(item.getLocation());
    }

    public void setLongDamage(double longDamage) { }

    public void setMidDamage(double midDamage) { }

    public void setShortDamage(double shortDamage) { }
}

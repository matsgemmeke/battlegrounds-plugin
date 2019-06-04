package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.event.GamePlayerKillPlayerEvent;
import com.matsg.battlegrounds.api.item.Lethal;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.util.Sound;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class BattleLethal extends BattleEquipment implements Lethal {

    private double longDamage, midDamage, shortDamage;

    public BattleLethal(
            Battlegrounds plugin,
            String id,
            String name,
            String description,
            ItemStack itemStack,
            int amount,
            int cooldown,
            double longDamage,
            double longRange,
            double midDamage,
            double midRange,
            double shortDamage,
            double shortRange,
            double velocity,
            int ignitionTime,
            Sound[] ignitionSound
    ) {
        super(plugin, id, name, description, itemStack, EquipmentType.LETHAL, amount, cooldown, longRange, midRange, shortRange, velocity,
                ignitionTime == 0 ? IgnitionType.PASSIVE : IgnitionType.AGGRESSIVE, ignitionTime, ignitionSound);
        this.longDamage = longDamage;
        this.midDamage = midDamage;
        this.shortDamage = shortDamage;
    }

    public Lethal clone() {
        return (Lethal) super.clone();
    }

    public double getLongDamage() {
        return longDamage;
    }

    public void setLongDamage(double longDamage) {
        this.longDamage = longDamage;
    }

    public double getMidDamage() {
        return midDamage;
    }

    public void setMidDamage(double midDamage) {
        this.midDamage = midDamage;
    }

    public double getShortDamage() {
        return shortDamage;
    }

    public void setShortDamage(double shortDamage) {
        this.shortDamage = shortDamage;
    }

    public void explode(Location location) {
        displayCircleEffect(location, 3, "EXPLOSION_LARGE", 1, 6);
        inflictDamage(location);
        inflictUserDamage(location);
    }

    public double getDamage(Hitbox hitbox, double distance) {
        return getDistanceDamage(distance);
    }

    private double getDistanceDamage(double distance) {
        if (distance <= shortRange) {
            return shortDamage;
        } else if (distance > shortRange && distance <= midRange) {
            return midDamage;
        } else if (distance > midRange && distance <= longRange) {
            return longDamage;
        }
        return 0.0;
    }

    public void ignite(Item item) {
        explode(item.getLocation());
    }

    private void inflictDamage(Location location) {
        for (GamePlayer gamePlayer : game.getPlayerManager().getNearbyPlayers(location, longRange)) {
            if (gamePlayer != null && gamePlayer != this.gamePlayer && !gamePlayer.getPlayer().isDead() && (gamePlayer.getTeam() != null && this.gamePlayer.getTeam() != gamePlayer.getTeam())) {
                game.getPlayerManager().damagePlayer(gamePlayer, getDistanceDamage(gamePlayer.getLocation().distanceSquared(location) / 5));
                if (gamePlayer.getPlayer().isDead()) {
                    plugin.getServer().getPluginManager().callEvent(new GamePlayerKillPlayerEvent(game, gamePlayer, this.gamePlayer, this, Hitbox.TORSO));
                    game.getGameMode().onKill(gamePlayer, this.gamePlayer, this, Hitbox.TORSO);
                }
            }
        }
    }

    private void inflictUserDamage(Location location) {
        double playerDistance = gamePlayer.getPlayer().getLocation().distanceSquared(location);
        if (playerDistance <= longRange) {
            game.getPlayerManager().damagePlayer(gamePlayer, getDistanceDamage(playerDistance));
            if (gamePlayer.getPlayer().isDead()) {
                plugin.getServer().getPluginManager().callEvent(new GamePlayerDeathEvent(game, gamePlayer, DeathCause.SUICIDE));
                game.getGameMode().onDeath(gamePlayer, DeathCause.SUICIDE);
            }
        }
    }
}

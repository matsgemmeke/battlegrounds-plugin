package com.matsg.battlegrounds.entity;

import com.matsg.battlegrounds.api.entity.*;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.util.GenericAttribute;
import com.matsg.battlegrounds.item.modifier.FloatAttributeModifier;
import com.matsg.battlegrounds.util.BattleAttribute;
import com.matsg.battlegrounds.util.data.FloatValueObject;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class BattleGamePlayer implements GamePlayer {

    private BattleEntityType entityType;
    private GenericAttribute<Float> reviveDuration;
    private int deaths, exp, headshots, kills, lives, points;
    private List<Item> items;
    private List<PlayerEffect> effects;
    private Loadout loadout, selectedLoadout;
    private Location returnLocation;
    private Player player;
    private PlayerState playerState;
    private SavedInventory savedInventory;
    private Team team;

    public BattleGamePlayer(Player player, SavedInventory savedInventory) {
        this.player = player;
        this.savedInventory = savedInventory;
        this.effects = new ArrayList<>();
        this.entityType = BattleEntityType.PLAYER;
        this.exp = 0;
        this.deaths = 0;
        this.headshots = 0;
        this.items = new ArrayList<>();
        this.kills = 0;
        this.lives = 0;
        this.playerState = PlayerState.ACTIVE;
        this.reviveDuration = new BattleAttribute<>("revive-duration", new FloatValueObject(10.0f));
    }

    public Entity getBukkitEntity() {
        return player;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public List<PlayerEffect> getEffects() {
        return effects;
    }

    public BattleEntityType getEntityType() {
        return entityType;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getHeadshots() {
        return headshots;
    }

    public void setHeadshots(int headshots) {
        this.headshots = headshots;
    }

    public float getHealth() {
        return (float) player.getHealth();
    }

    public void setHealth(float health) {
        player.setHealth(health);
    }

    public List<Item> getItems() {
        return items;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public Loadout getLoadout() {
        return loadout;
    }

    public void setLoadout(Loadout loadout) {
        this.loadout = loadout;
    }

    public Location getLocation() {
        return player.getLocation();
    }

    public float getMaxHealth() {
        return (float) player.getMaxHealth();
    }

    public void setMaxHealth(float maxHealth) {
        player.setMaxHealth(maxHealth);
    }

    public String getName() {
        return player.getName();
    }

    public Player getPlayer() {
        return player;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Location getReturnLocation() {
        return returnLocation;
    }

    public void setReturnLocation(Location returnLocation) {
        this.returnLocation = returnLocation;
    }

    public float getReviveDuration() {
        return reviveDuration.getValue();
    }

    public void setReviveDuration(float reviveDuration) {
        this.reviveDuration.applyModifier(new FloatAttributeModifier(reviveDuration));
    }

    public SavedInventory getSavedInventory() {
        return savedInventory;
    }

    public Loadout getSelectedLoadout() {
        return selectedLoadout;
    }

    public void setSelectedLoadout(Loadout selectedLoadout) {
        this.selectedLoadout = selectedLoadout;
    }

    public PlayerState getState() {
        return playerState;
    }

    public void setState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public UUID getUUID() {
        return player.getUniqueId();
    }

    public boolean isOnline() {
        return player.isOnline();
    }

    public int addExp(int exp) {
        this.exp += exp;
        return this.exp;
    }

    public int compareTo(GamePlayer gamePlayer) {
        if (exp != gamePlayer.getExp()) {
            return gamePlayer.getExp() - exp;
        }
        if (kills != gamePlayer.getKills()) {
            return gamePlayer.getKills() - kills;
        }
        return 0;
    }

    public double damage(double damage) {
        if (player.isDead() || !playerState.isAlive()) {
            return 0.0;
        }

        double finalHealth = player.getHealth() - damage;

        player.damage(0.01); // Create fake damage animation
        player.setHealth(finalHealth > 0.0 ? finalHealth : 0); // Set the health to 0 if the damage is greater than the health
        player.setLastDamageCause(null);

        return player.getHealth();
    }

    public boolean isHostileTowards(GamePlayer gamePlayer) {
        return team != gamePlayer.getTeam();
    }

    public void refreshEffects() {
        for (PlayerEffect effect : effects) {
            effect.refresh();
        }
    }

    public void remove() {
        player.remove();
    }

    public void sendMessage(String message) {
        player.sendMessage(message);
    }
}

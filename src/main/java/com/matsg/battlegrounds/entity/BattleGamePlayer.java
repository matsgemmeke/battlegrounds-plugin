package com.matsg.battlegrounds.entity;

import com.matsg.battlegrounds.api.entity.PlayerState;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.SavedInventory;
import com.matsg.battlegrounds.api.item.Perk;
import com.matsg.battlegrounds.api.util.GenericAttribute;
import com.matsg.battlegrounds.item.modifier.FloatAttributeModifier;
import com.matsg.battlegrounds.util.BattleAttribute;
import com.matsg.battlegrounds.util.valueobject.FloatValueObject;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class BattleGamePlayer implements GamePlayer {

    private GenericAttribute<Float> fireArmDamage, reloadSpeed, reviveSpeed;
    private int deaths, exp, headshots, kills, lives, points;
    private List<Item> heldItems;
    private Loadout loadout, selectedLoadout;
    private Player player;
    private PlayerState playerState;
    private SavedInventory savedInventory;
    private Set<Perk> perks;
    private Team team;

    public BattleGamePlayer(Player player, SavedInventory savedInventory) {
        this.player = player;
        this.savedInventory = savedInventory;
        this.exp = 0;
        this.deaths = 0;
        this.fireArmDamage = new BattleAttribute<>("firearm-damage", new FloatValueObject((float) 1.0));
        this.headshots = 0;
        this.heldItems = new ArrayList<>();
        this.kills = 0;
        this.lives = 0;
        this.playerState = PlayerState.ACTIVE;
        this.perks = new HashSet<>();
        this.reloadSpeed = new BattleAttribute<>("reload-speed", new FloatValueObject((float) 1.0));
        this.reviveSpeed = new BattleAttribute<>("revive-speed", new FloatValueObject((float) 1.0));
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

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public double getFirearmDamage() {
        return fireArmDamage.getValue();
    }

    public void setFirearmDamage(double firearmDamage) {
        this.fireArmDamage.applyModifier(new FloatAttributeModifier((float) firearmDamage));
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

    public List<Item> getHeldItems() {
        return heldItems;
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

    public Set<Perk> getPerks() {
        return perks;
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

    public double getReloadSpeed() {
        return reloadSpeed.getValue();
    }

    public void setReloadSpeed(double reloadSpeed) {
        this.reloadSpeed.applyModifier(new FloatAttributeModifier((float) reloadSpeed));
    }

    public double getReviveSpeed() {
        return reviveSpeed.getValue();
    }

    public void setReviveSpeed(double reviveSpeed) {
        this.reviveSpeed.applyModifier(new FloatAttributeModifier((float) reviveSpeed));
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

    public void remove() {
        player.remove();
    }

    public void sendMessage(String message) {
        player.sendMessage(message);
    }
}

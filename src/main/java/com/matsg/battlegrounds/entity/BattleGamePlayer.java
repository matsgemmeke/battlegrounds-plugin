package com.matsg.battlegrounds.entity;

import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.PlayerStatus;
import com.matsg.battlegrounds.api.entity.SavedInventory;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BattleGamePlayer implements GamePlayer {

    private int deaths, exp, headshots, kills, lives;
    private List<Item> heldItems;
    private Loadout loadout, selectedLoadout;
    private Player player;
    private PlayerStatus playerStatus;
    private SavedInventory savedInventory;
    private Team team;

    public BattleGamePlayer(Player player, SavedInventory savedInventory) {
        this.player = player;
        this.exp = 0;
        this.deaths = 0;
        this.headshots = 0;
        this.heldItems = new ArrayList<>();
        this.kills = 0;
        this.lives = 0;
        this.playerStatus = PlayerStatus.ACTIVE;
        this.savedInventory = savedInventory;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getExp() {
        return exp;
    }

    public int getHeadshots() {
        return headshots;
    }

    public List<Item> getHeldItems() {
        return heldItems;
    }

    public int getKills() {
        return kills;
    }

    public int getLives() {
        return lives;
    }

    public Loadout getLoadout() {
        return loadout;
    }

    public Player getPlayer() {
        return player;
    }

    public SavedInventory getSavedInventory() {
        return savedInventory;
    }

    public Loadout getSelectedLoadout() {
        return selectedLoadout;
    }

    public PlayerStatus getStatus() {
        return playerStatus;
    }

    public Team getTeam() {
        return team;
    }

    public UUID getUUID() {
        return player.getUniqueId();
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setHeadshots(int headshots) {
        this.headshots = headshots;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setLoadout(Loadout loadout) {
        this.loadout = loadout;
    }

    public void setSelectedLoadout(Loadout selectedLoadout) {
        this.selectedLoadout = selectedLoadout;
    }

    public PlayerStatus setStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
        return playerStatus;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int addExp(int exp) {
        this.exp += exp;
        return exp;
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

    public Entity getBukkitEntity() {
        return player;
    }

    public double getHealth() {
        return player.getHealth();
    }

    public Location getLocation() {
        return player.getLocation();
    }

    public double getMaxHealth() {
        return player.getMaxHealth();
    }

    public String getName() {
        return player.getName();
    }

    public boolean isOnline() {
        return player.isOnline();
    }

    public void remove() {
        player.remove();
    }

    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    public void setHealth(double health) {
        player.setHealth(health);
    }

    public void setMaxHealth(double maxHealth) {
        player.setMaxHealth(maxHealth);
    }
}

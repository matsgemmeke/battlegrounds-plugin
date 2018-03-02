package com.matsg.battlegrounds.player;

import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.player.OfflineGamePlayer;
import com.matsg.battlegrounds.api.player.PlayerStatus;
import com.matsg.battlegrounds.api.player.SavedInventory;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.util.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class BattleGamePlayer implements GamePlayer {

    private int deaths, exp, headshots, kills, score;
    private LoadoutClass loadoutClass;
    private Player player;
    private PlayerStatus playerStatus;
    private SavedInventory savedInventory;

    public BattleGamePlayer(Player player) {
        this.player = player;
        this.exp = 0;
        this.deaths = 0;
        this.headshots = 0;
        this.kills = 0;
        this.playerStatus = PlayerStatus.ACTIVE;
        this.savedInventory = new BattleSavedInventory(player);
        this.score = 0;
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

    public int getKills() {
        return kills;
    }

    public LoadoutClass getLoadoutClass() {
        return loadoutClass;
    }

    public Location getLocation() {
        return player.getLocation();
    }

    public String getName() {
        return player.getName();
    }

    public Player getPlayer() {
        return player;
    }

    public SavedInventory getSavedInventory() {
        return savedInventory;
    }

    public int getScore() {
        return score;
    }

    public PlayerStatus getStatus() {
        return playerStatus;
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

    public void setLoadoutClass(LoadoutClass loadoutClass) {
        this.loadoutClass = loadoutClass;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public PlayerStatus setStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
        return playerStatus;
    }

    public int addScore(int score) {
        this.score += score;
        return score;
    }

    public int compareTo(OfflineGamePlayer gamePlayer) {
        if (exp != gamePlayer.getExp()) {
            return gamePlayer.getExp() - exp;
        }
        if (kills != gamePlayer.getKills()) {
            return gamePlayer.getKills() - kills;
        }
        return 0;
    }

    public void sendMessage(Message message) {
        message.send(player);
    }

    public void sendMessage(String message) {
        player.sendMessage(message);
    }
}
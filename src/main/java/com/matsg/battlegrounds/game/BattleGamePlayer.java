package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.GamePlayer;
import com.matsg.battlegrounds.api.game.PlayerStatus;
import com.matsg.battlegrounds.api.game.SavedInventory;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.util.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BattleGamePlayer implements GamePlayer {

    private FireArm primary, secondary;
    private int deaths, headshots, kills, points;
    private Knife knife;
    private Lethal lethal;
    private Player player;
    private PlayerStatus playerStatus;
    private SavedInventory savedInventory;
    private Tactical tactical;

    public BattleGamePlayer(Player player) {
        this.player = player;
        this.deaths = 0;
        this.headshots = 0;
        this.kills = 0;
        this.playerStatus = PlayerStatus.ALIVE;
        this.points = 0;
        this.savedInventory = new BattleSavedInventory(player);
    }

    public int getDeaths() {
        return deaths;
    }

    public int getHeadshots() {
        return headshots;
    }

    public int getKills() {
        return kills;
    }

    public Knife getKnife() {
        return knife;
    }

    public Lethal getLethal() {
        return lethal;
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

    public int getPoints() {
        return points;
    }

    public FireArm getPrimary() {
        return primary;
    }

    public SavedInventory getSavedInventory() {
        return savedInventory;
    }

    public FireArm getSecondary() {
        return secondary;
    }

    public PlayerStatus getStatus() {
        return playerStatus;
    }

    public Tactical getTactical() {
        return tactical;
    }

    public UUID getUUID() {
        return player.getUniqueId();
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setHeadshots(int headshots) {
        this.headshots = headshots;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setKnife(Knife knife) {
        this.knife = knife;
    }

    public void setLethal(Lethal lethal) {
        this.lethal = lethal;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setPrimary(FireArm primary) {
        this.primary = primary;
    }

    public void setSecondary(FireArm secondary) {
        this.secondary = secondary;
    }

    public PlayerStatus setStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
        return playerStatus;
    }

    public void setTactical(Tactical tactical) {
        this.tactical = tactical;
    }

    public Weapon getWeapon(ItemSlot itemSlot) {
        for (Weapon weapon : getWeapons()) {
            if (weapon != null && weapon.getItemSlot() == itemSlot) {
                return weapon;
            }
        }
        return null;
    }

    public Weapon getWeapon(ItemStack itemStack) {
        for (Weapon weapon : getWeapons()) {
            if (weapon != null && weapon.getItemStack().equals(itemStack)) {
                return weapon;
            }
        }
        return null;
    }

    public Weapon getWeaponIgnoreMetadata(ItemStack itemStack) {
        for (Weapon weapon : getWeapons()) {
            if (weapon != null) {
                ItemStack other = weapon.getItemStack();
                if (other != null && other.getAmount() == itemStack.getAmount()
                        && other.getDurability() == itemStack.getDurability() && other.getType() == itemStack.getType()) {
                    return weapon;
                }
            }
        }
        return null;
    }

    public Weapon[] getWeapons() {
        return new Weapon[] { primary, secondary, lethal, tactical, knife };
    }

    public void sendMessage(Message message) {
        message.send(player);
    }

    public void sendMessage(String message) {
        player.sendMessage(message);
    }
}
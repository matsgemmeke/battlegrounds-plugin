package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.util.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface GamePlayer {

    int getDeaths();

    int getHeadshots();

    int getKills();

    Knife getKnife();

    Lethal getLethal();

    Location getLocation();

    String getName();

    Player getPlayer();

    FireArm getPrimary();

    int getPoints();

    SavedInventory getSavedInventory();

    FireArm getSecondary();

    PlayerStatus getStatus();

    Tactical getTactical();

    Weapon getWeapon(ItemSlot itemSlot);

    Weapon getWeapon(ItemStack itemStack);

    Weapon getWeaponIgnoreMetadata(ItemStack itemStack);

    Weapon[] getWeapons();

    UUID getUUID();

    void sendMessage(Message message);

    void sendMessage(String message);

    void setDeaths(int deaths);

    void setHeadshots(int headshots);

    void setKills(int kills);

    void setKnife(Knife knife);

    void setLethal(Lethal lethal);

    void setPrimary(FireArm primary);

    void setSecondary(FireArm secondary);

    void setPoints(int points);

    PlayerStatus setStatus(PlayerStatus playerStatus);

    void setTactical(Tactical tactical);
}
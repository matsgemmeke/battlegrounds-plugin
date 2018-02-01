package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.item.FireArm;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.Knife;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.util.Message;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface GamePlayer {

    int getDowns();

    Explosive getExplosive();

    int getHeadshots();

    int getKills();

    Knife getKnife();

    String getName();

    Player getPlayer();

    PlayerStatus getPlayerStatus();

    FireArm getPrimary();

    int getPoints();

    int getRevives();

    SavedInventory getSavedInventory();

    FireArm getSecondary();

    Weapon getWeapon(ItemSlot itemSlot);

    Weapon getWeapon(ItemStack itemStack);

    Weapon getWeaponIgnoreMetadata(ItemStack itemStack);

    Weapon[] getWeapons();

    UUID getUUID();

    void sendMessage(Message message);

    void sendMessage(String message);

    void setDowns(int downs);

    void setExplosive(Explosive explosive);

    void setHeadshots(int headshots);

    void setKills(int kills);

    void setKnife(Knife knife);

    void setPlayerStatus(PlayerStatus playerStatus);

    void setPrimary(FireArm primary);

    void setRevives(int revives);

    void setSecondary(FireArm secondary);

    void setPoints(int points);
}
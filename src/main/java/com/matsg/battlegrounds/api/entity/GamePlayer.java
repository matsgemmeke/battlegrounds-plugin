package com.matsg.battlegrounds.api.entity;

import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.item.Perk;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Set;

public interface GamePlayer extends BaseEntity, OfflineGamePlayer, Comparable<GamePlayer> {

    Collection<Item> getHeldItems();

    double getFirearmDamage();

    void setFirearmDamage(double firearmDamage);

    int getLives();

    void setLives(int lives);

    Loadout getLoadout();

    void setLoadout(Loadout loadout);

    Set<Perk> getPerks();

    Player getPlayer();

    int getPoints();

    void setPoints(int points);

    double getReloadSpeed();

    void setReloadSpeed(double reloadSpeed);

    double getReviveSpeed();

    void setReviveSpeed(double reviveSpeed);

    SavedInventory getSavedInventory();

    Loadout getSelectedLoadout();

    void setSelectedLoadout(Loadout loadout);

    PlayerState getState();

    void setState(PlayerState playerState);

    Team getTeam();

    void setTeam(Team team);

    int addExp(int exp);

    void sendMessage(String message);
}

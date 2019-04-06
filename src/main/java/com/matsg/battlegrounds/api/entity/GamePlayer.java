package com.matsg.battlegrounds.api.entity;

import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.item.Perk;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Set;

public interface GamePlayer extends BaseEntity, OfflineGamePlayer, Comparable<GamePlayer> {

    int addExp(int exp);

    Collection<Item> getHeldItems();

    int getLives();

    Loadout getLoadout();

    Set<Perk> getPerks();

    Player getPlayer();

    int getPoints();

    SavedInventory getSavedInventory();

    Loadout getSelectedLoadout();

    PlayerStatus getStatus();

    Team getTeam();

    void sendMessage(String message);

    void setLives(int lives);

    void setLoadout(Loadout loadout);

    void setPoints(int points);

    void setSelectedLoadout(Loadout loadout);

    void setTeam(Team team);

    PlayerStatus setStatus(PlayerStatus playerStatus);
}

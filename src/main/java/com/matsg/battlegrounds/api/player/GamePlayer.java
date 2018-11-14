package com.matsg.battlegrounds.api.player;

import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.Loadout;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface GamePlayer extends OfflineGamePlayer, Comparable<GamePlayer> {

    int addExp(int exp);

    Collection<Item> getHeldItems();

    int getLives();

    Loadout getLoadout();

    Location getLocation();

    Player getPlayer();

    SavedInventory getSavedInventory();

    Loadout getSelectedLoadout();

    PlayerStatus getStatus();

    Team getTeam();

    void sendMessage(String message);

    void setLives(int lives);

    void setLoadout(Loadout loadout);

    void setSelectedLoadout(Loadout loadout);

    void setTeam(Team team);

    PlayerStatus setStatus(PlayerStatus playerStatus);
}
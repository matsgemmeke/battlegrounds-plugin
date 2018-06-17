package com.matsg.battlegrounds.api.player;

import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.util.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface GamePlayer extends OfflineGamePlayer, Comparable<GamePlayer> {

    int addScore(int score);

    Collection<Item> getHeldItems();

    Loadout getLoadout();

    Location getLocation();

    Player getPlayer();

    SavedInventory getSavedInventory();

    int getScore();

    PlayerStatus getStatus();

    Team getTeam();

    void sendMessage(Message message);

    void sendMessage(String message);

    void setLoadout(Loadout loadout);

    void setScore(int score);

    void setTeam(Team team);

    PlayerStatus setStatus(PlayerStatus playerStatus);
}
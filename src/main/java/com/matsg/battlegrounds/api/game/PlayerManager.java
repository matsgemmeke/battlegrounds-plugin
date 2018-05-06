package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.item.LoadoutClass;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface PlayerManager {

    GamePlayer addPlayer(Player player);

    void changeLoadoutClass(GamePlayer gamePlayer, LoadoutClass loadoutClass);

    void damagePlayer(GamePlayer gamePlayer, double damage);

    GamePlayer getGamePlayer(Player player);

    GamePlayer[] getLivingPlayers();

    GamePlayer[] getLivingPlayers(Team team);

    GamePlayer[] getNearbyPlayers(Location location, double range);

    GamePlayer getNearestPlayer(Location location);

    GamePlayer getNearestPlayer(Location location, double range);

    GamePlayer getNearestPlayer(Location location, Team team);

    GamePlayer getNearestPlayer(Location location, Team team, double range);

    Collection<GamePlayer> getPlayers();

    void removePlayer(Player player);

    void setVisible(GamePlayer gamePlayer, boolean visible);

    void setVisible(GamePlayer gamePlayer, Team team, boolean visible);
}
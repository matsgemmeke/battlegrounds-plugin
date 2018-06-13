package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface PlayerManager {

    GamePlayer addPlayer(Player player);

    void changeLoadout(GamePlayer gamePlayer, Loadout loadout, boolean apply);

    void damagePlayer(GamePlayer gamePlayer, double damage);

    GamePlayer getGamePlayer(Player player);

    GamePlayer[] getLivingPlayers();

    GamePlayer[] getLivingPlayers(Team team);

    GamePlayer[] getNearbyEnemyPlayers(Game game, GamePlayer gamePlayer, double range);

    GamePlayer[] getNearbyPlayers(Game game, Location location, double range);

    GamePlayer[] getNearbyPlayers(Location location, double range);

    GamePlayer getNearestPlayer(Location location);

    GamePlayer getNearestPlayer(Location location, double range);

    GamePlayer getNearestPlayer(Location location, Team team);

    GamePlayer getNearestPlayer(Location location, Team team, double range);

    Collection<GamePlayer> getPlayers();

    void onPlayerMove(Player player, Location from, Location to);

    void preparePlayer(GamePlayer gamePlayer);

    void receivePlayerChat(Player player, String message);

    void removePlayer(GamePlayer gamePlayer);

    void respawnPlayer(GamePlayer gamePlayer, Spawn spawn);

    void setVisible(GamePlayer gamePlayer, boolean visible);

    void setVisible(GamePlayer gamePlayer, Team team, boolean visible);
}
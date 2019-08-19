package com.matsg.battlegrounds.api.entity;

import com.matsg.battlegrounds.api.game.SpawnOccupant;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.Loadout;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface GamePlayer extends BattleEntity, OfflineGamePlayer, SpawnOccupant, Comparable<GamePlayer> {

    Collection<PlayerEffect> getEffects();

    Collection<Item> getItems();

    int getLives();

    void setLives(int lives);

    Loadout getLoadout();

    void setLoadout(Loadout loadout);

    Player getPlayer();

    int getPoints();

    void setPoints(int points);

    float getReviveDuration();

    void setReviveDuration(float reviveDuration);

    SavedInventory getSavedInventory();

    Loadout getSelectedLoadout();

    void setSelectedLoadout(Loadout loadout);

    PlayerState getState();

    void setState(PlayerState playerState);

    Team getTeam();

    void setTeam(Team team);

    int addExp(int exp);

    void refreshEffects();

    void sendMessage(String message);
}

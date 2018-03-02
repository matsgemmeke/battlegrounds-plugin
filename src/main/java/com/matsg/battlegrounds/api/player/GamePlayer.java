package com.matsg.battlegrounds.api.player;

import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.util.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface GamePlayer extends OfflineGamePlayer {

    int addScore(int score);

    LoadoutClass getLoadoutClass();

    Location getLocation();

    Player getPlayer();

    SavedInventory getSavedInventory();

    int getScore();

    PlayerStatus getStatus();

    void sendMessage(Message message);

    void sendMessage(String message);

    void setLoadoutClass(LoadoutClass loadoutClass);

    void setScore(int score);

    PlayerStatus setStatus(PlayerStatus playerStatus);
}
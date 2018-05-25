package com.matsg.battlegrounds.api.player;

import com.matsg.battlegrounds.api.config.StoredPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface PlayerStorage {

    void addPlayerAttributes(StoredPlayer player);

    boolean contains(UUID uuid);

    List<? extends OfflineGamePlayer> getList();

    StoredPlayer getStoredPlayer(UUID uuid);

    List<? extends OfflineGamePlayer> getTopPlayers(int limit);

    StoredPlayer registerPlayer(UUID uuid, String name);

    void updatePlayer(Player player);
}
package com.matsg.battlegrounds.api.config;

import com.matsg.battlegrounds.api.player.OfflineGamePlayer;

public interface StoredPlayer extends OfflineGamePlayer, Comparable<StoredPlayer> {

    int getAttribute(String attribute);

    PlayerYaml getPlayerYaml();
}
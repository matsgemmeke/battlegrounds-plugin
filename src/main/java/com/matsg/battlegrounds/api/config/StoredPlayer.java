package com.matsg.battlegrounds.api.config;

import com.matsg.battlegrounds.api.item.LoadoutClass;
import com.matsg.battlegrounds.api.player.OfflineGamePlayer;

import java.util.Collection;

public interface StoredPlayer extends OfflineGamePlayer, Comparable<StoredPlayer> {

    int getAttribute(String attribute);
}
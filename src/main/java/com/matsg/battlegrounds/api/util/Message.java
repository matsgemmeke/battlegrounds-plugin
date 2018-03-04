package com.matsg.battlegrounds.api.util;

import org.bukkit.entity.Player;

public interface Message extends Cloneable {

    String getMessage(Placeholder... placeholders);

    void send(Player player, Placeholder... placeholders);
}
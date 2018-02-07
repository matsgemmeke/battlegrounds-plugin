package com.matsg.battlegrounds.api.game;

import org.bukkit.event.Event;

public @interface EventHandler {

    Class<? extends Event>[] events();
}
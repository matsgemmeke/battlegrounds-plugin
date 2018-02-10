package com.matsg.battlegrounds.api;

import org.bukkit.plugin.Plugin;

public interface BattlegroundsExtension extends Plugin {

    void onInit();

    void onPostInit();
}
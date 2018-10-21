package com.matsg.battlegrounds.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Version {

    void sendActionBar(Player player, String message);

    void sendJSONMessage(Player player, String message, String command, String hoverMessage);

    void sendTitle(Player player, String title, String subTitle, int fadeIn, int time, int fadeOut);

    void spawnParticle(Location location, String effect, int amount, float offsetX, float offsetY, float offsetZ, float speed);

    boolean supports(String version);
}

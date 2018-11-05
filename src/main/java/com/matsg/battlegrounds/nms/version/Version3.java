package com.matsg.battlegrounds.nms.version;

import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.nms.ReflectionUtils;
import com.matsg.battlegrounds.nms.Title;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class Version3 implements Version {

    public void sendActionBar(Player player, String message) {
        try {
            Object chatMessageType = ReflectionUtils.getNMSClass("ChatMessageType").getField("GAME_INFO").get(null);
            Object icbc = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                    .getMethod("a", String.class).invoke(null, "{\"text\":\"" + message + "\"}");

            Constructor actionBarConstructor = ReflectionUtils.getNMSClass("PacketPlayOutChat")
                    .getConstructor(ReflectionUtils.getNMSClass("IChatBaseComponent"),
                            ReflectionUtils.getNMSClass("ChatMessageType"));

            Object actionBarPacket = actionBarConstructor.newInstance(icbc, chatMessageType);

            ReflectionUtils.sendPacket(player, actionBarPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendJSONMessage(Player player, String message, String command, String hoverMessage) {
        String text = "{\"text\":\"\",\"extra\":[{\"text\":\"" + message + "\"," +
                "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + hoverMessage + "\"}," +
                "\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + command + "\"}}]}";

        try {
            Object chatMessageType = ReflectionUtils.getNMSClass("ChatMessageType").getField("CHAT").get(null);
            Object icbc = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                    .getMethod("a", String.class).invoke(null, text);

            Constructor jsonConstructor = ReflectionUtils.getNMSClass("PacketPlayOutChat")
                    .getConstructor(ReflectionUtils.getNMSClass("IChatBaseComponent"),
                            ReflectionUtils.getNMSClass("ChatMessageType"));

            Object jsonPacket = jsonConstructor.newInstance(icbc, chatMessageType);

            ReflectionUtils.sendPacket(player, jsonPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTitle(Player player, String title, String subTitle, int fadeIn, int time, int fadeOut) {
        new Title(title, subTitle, fadeIn, time, fadeOut).send(player);
    }

    public void spawnColoredParticle(Location location, String effect, float red, float green, float blue) {
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 0, red, green, blue, new DustOptions(Color.WHITE, 1)); // Look for a way to include RBG colors
    }

    public void spawnParticle(Location location, String effect, int amount, float offsetX, float offsetY, float offsetZ, int speed) {
        location.getWorld().spawnParticle(Particle.valueOf(effect), location, amount, offsetX, offsetY, offsetZ);
    }

    public boolean supports(String version) {
        String[] split = version.split("_");
        return split.length == 3 && Integer.parseInt(split[1]) >= 13;
    }
}

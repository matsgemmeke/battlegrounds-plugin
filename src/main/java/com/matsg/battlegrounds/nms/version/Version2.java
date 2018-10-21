package com.matsg.battlegrounds.nms.version;

import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.nms.ReflectionUtils;
import com.matsg.battlegrounds.util.Particle;
import com.matsg.battlegrounds.util.Particle.ParticleEffect;
import com.matsg.battlegrounds.util.Title;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class Version2 implements Version {

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

    public void spawnParticle(Location location, String effect, int amount, float offsetX, float offsetY, float offsetZ, float speed) {
        Particle particle = new Particle(ParticleEffect.valueOf(effect), 0, location, 0, 0, 0, speed);
        particle.setOffsetX(offsetX);
        particle.setOffsetY(offsetY);
        particle.setOffsetZ(offsetZ);
        particle.display();
    }

    public boolean supports(String version) {
        String[] split = version.split("_");
        return split.length == 3 && Integer.parseInt(split[1]) == 12;
    }
}

package com.matsg.battlegrounds.nms.version;

import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.nms.Particle;
import com.matsg.battlegrounds.nms.Particle.ParticleEffect;
import com.matsg.battlegrounds.nms.ReflectionUtils;
import com.matsg.battlegrounds.nms.Title;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class Version1 implements Version {

    public void sendActionBar(Player player, String message) {
        try {
            Object icbc = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                    .getMethod("a", String.class).invoke(null, "{\"text\":\"" + message + "\"}");

            Constructor actionBarConstructor = ReflectionUtils.getNMSClass("PacketPlayOutChat")
                    .getConstructor(ReflectionUtils.getNMSClass("IChatBaseComponent"), byte.class);

            Object actionBarPacket = actionBarConstructor.newInstance(icbc, (byte) 2);

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
            Object icbc = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                    .getMethod("a", String.class).invoke(null, text);

            Constructor jsonConstructor = ReflectionUtils.getNMSClass("PacketPlayOutChat")
                    .getConstructor(ReflectionUtils.getNMSClass("IChatBaseComponent"), byte.class);

            Object jsonPacket = jsonConstructor.newInstance(icbc, (byte) 0);

            ReflectionUtils.sendPacket(player, jsonPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTitle(Player player, String title, String subTitle, int fadeIn, int time, int fadeOut) {
        new Title(title, subTitle, fadeIn, time, fadeOut).send(player);
    }

    public void spawnColoredParticle(Location location, String effect, float red, float green, float blue) {
        Particle particle = new Particle(ParticleEffect.valueOf(effect), 0, location, 0, 0, 0, 1);
        particle.setOffsetX(red);
        particle.setOffsetY(green);
        particle.setOffsetZ(blue);
        particle.display();
    }

    public void spawnParticle(Location location, String effect, int amount, float offsetX, float offsetY, float offsetZ, int speed) {
        new Particle(ParticleEffect.valueOf(effect), amount, location, offsetX, offsetY, offsetZ, speed).display();
    }

    public boolean supports(String version) {
        String[] split = version.split("_");
        return split.length == 3 && Integer.parseInt(split[1]) < 12;
    }
}

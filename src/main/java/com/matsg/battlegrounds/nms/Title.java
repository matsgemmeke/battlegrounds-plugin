package com.matsg.battlegrounds.nms;

import com.matsg.battlegrounds.api.Placeholder;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class Title {

    private int fadeIn, fadeOut, time;
    private String subTitle, title;

    public Title(String title, String subTitle, int fadeIn, int time, int fadeOut) {
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.time = time;
        this.fadeOut = fadeOut;
    }

    public String getMessage() {
        return title + " " + subTitle;
    }

    public void send(Player player, Placeholder... placeholders) {
        send(player, title, subTitle, fadeIn, time, fadeOut, placeholders);
    }

    private void send(Player player, String title, String subTitle, int fadeIn, int time, int fadeOut, Placeholder... placeholders) {
        if (title != null) {
            try {
                Object enumTitle = ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0]
                        .getField("TIMES").get(null);
                Object chatTitle = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                        .getMethod("a", String.class)
                        .invoke(null, "{\"text\":\"" + title + "\"}");

                Constructor titleConstructor = ReflectionUtils.getNMSClass("PacketPlayOutTitle")
                        .getConstructor(ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                                ReflectionUtils.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);

                Object titlePacket = titleConstructor.newInstance(enumTitle, chatTitle, fadeIn, time, fadeOut);

                ReflectionUtils.sendPacket(player, titlePacket);

                enumTitle = ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0]
                        .getField("TITLE").get(null);

                chatTitle = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                        .getMethod("a", String.class)
                        .invoke(null, "{\"text\":\"" + title + "\"}");

                titleConstructor = ReflectionUtils.getNMSClass("PacketPlayOutTitle")
                        .getConstructor(ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                                ReflectionUtils.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);

                titlePacket = titleConstructor.newInstance(enumTitle, chatTitle, fadeIn, time, fadeOut);

                ReflectionUtils.sendPacket(player, titlePacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (subTitle != null) {
            try {
                Object enumSubTitle = ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0]
                        .getField("TIMES").get(null);
                Object chatSubTitle = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                        .getMethod("a", String.class)
                        .invoke(null, "{\"text\":\"" + subTitle + "\"}");

                Constructor subTitleConstructor = ReflectionUtils.getNMSClass("PacketPlayOutTitle")
                        .getConstructor(ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                                ReflectionUtils.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);

                Object subTitlePacket = subTitleConstructor.newInstance(enumSubTitle, chatSubTitle, fadeIn, time, fadeOut);

                ReflectionUtils.sendPacket(player, subTitlePacket);

                enumSubTitle = ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0]
                        .getField("SUBTITLE").get(null);

                chatSubTitle = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                        .getMethod("a", String.class)
                        .invoke( null, "{\"text\":\"" + subTitle + "\"}");

                subTitleConstructor = ReflectionUtils.getNMSClass("PacketPlayOutTitle")
                        .getConstructor(ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                                ReflectionUtils.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);

                subTitlePacket = subTitleConstructor.newInstance(enumSubTitle, chatSubTitle, fadeIn, time, fadeOut);

                ReflectionUtils.sendPacket(player, subTitlePacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String toString() {
        return title + "," + subTitle;
    }
}

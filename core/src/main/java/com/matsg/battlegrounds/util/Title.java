package com.matsg.battlegrounds.util;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class Title implements Cloneable {

    private int fadeIn, fadeOut, time;
    private String subText, titleText;

    public Title(String titleText, String subText, int fadeIn, int time, int fadeOut) {
        this.titleText = titleText;
        this.subText = subText;
        this.fadeIn = fadeIn;
        this.time = time;
        this.fadeOut = fadeOut;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public void setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public void setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public Title clone() {
        try {
            return (Title) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMessage() {
        return titleText + " " + subText;
    }

    public void send(Player player) {
        send(player, titleText, subText, fadeIn, time, fadeOut);
    }

    private void send(Player player, String titleText, String subText, int fadeIn, int time, int fadeOut) {
        if (titleText != null) {
            try {
                Object enumTitle = ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0]
                        .getField("TIMES").get(null);
                Object chatTitle = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                        .getMethod("a", String.class)
                        .invoke(null, "{\"text\":\"" + titleText + "\"}");

                Constructor titleConstructor = ReflectionUtils.getNMSClass("PacketPlayOutTitle")
                        .getConstructor(ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                                ReflectionUtils.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);

                Object titlePacket = titleConstructor.newInstance(enumTitle, chatTitle, fadeIn, time, fadeOut);

                ReflectionUtils.sendPacket(player, titlePacket);

                enumTitle = ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0]
                        .getField("TITLE").get(null);

                chatTitle = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                        .getMethod("a", String.class)
                        .invoke(null, "{\"text\":\"" + titleText + "\"}");

                titleConstructor = ReflectionUtils.getNMSClass("PacketPlayOutTitle")
                        .getConstructor(ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                                ReflectionUtils.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);

                titlePacket = titleConstructor.newInstance(enumTitle, chatTitle, fadeIn, time, fadeOut);

                ReflectionUtils.sendPacket(player, titlePacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (subText != null) {
            try {
                Object enumSubTitle = ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0]
                        .getField("TIMES").get(null);
                Object chatSubTitle = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                        .getMethod("a", String.class)
                        .invoke(null, "{\"text\":\"" + subText + "\"}");

                Constructor subTitleConstructor = ReflectionUtils.getNMSClass("PacketPlayOutTitle")
                        .getConstructor(ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                                ReflectionUtils.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);

                Object subTitlePacket = subTitleConstructor.newInstance(enumSubTitle, chatSubTitle, fadeIn, time, fadeOut);

                ReflectionUtils.sendPacket(player, subTitlePacket);

                enumSubTitle = ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0]
                        .getField("SUBTITLE").get(null);

                chatSubTitle = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                        .getMethod("a", String.class)
                        .invoke( null, "{\"text\":\"" + subText + "\"}");

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
        return titleText + "," + subText;
    }
}

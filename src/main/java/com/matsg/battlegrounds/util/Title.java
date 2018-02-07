package com.matsg.battlegrounds.util;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.util.Message;
import com.matsg.battlegrounds.api.util.Placeholder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public enum Title implements Message {

    COUNTDOWN("title-countdown");

    private int fadeIn, fadeOut, time;
    private String path, subTitle, title;

    Title(String path) {
        if (path == null || path.length() <= 0) {
            throw new TitleFormatException("Title argument cannot be null");
        }
        String string = BattlegroundsPlugin.getPlugin().getTranslator().getTranslation(path);
        String[] split = string.split(",");
        if (split.length <= 4) {
            throw new TitleFormatException("Invalid title format \"" + string + "\"");
        }
        try {
            this.title = split[0];
            this.subTitle = split[1];
            this.fadeIn = Integer.parseInt(split[2]);
            this.time = Integer.parseInt(split[3]);
            this.fadeOut = Integer.parseInt(split[4]);
        } catch (Exception e) {
            throw new TitleFormatException("An error occurred while formatting the title");
        }
        this.path = path;
    }

    public String getMessage() {
        return title + " " + subTitle;
    }

    public String getMessage(Placeholder... placeholders) {
        return replace(title, placeholders) + " " + replace(subTitle, placeholders);
    }

    private String replace(String string, Placeholder... placeholders) {
        return ChatColor.translateAlternateColorCodes('&', Placeholder.replace(string, placeholders));
    }

    public void send(Player player, Placeholder... placeholders) {
        send(player, title, subTitle, fadeIn, time, fadeOut, placeholders);
    }
    
    private void send(Player player, String title, String subTitle, int fadeIn, int time, int fadeOut, Placeholder... placeholders) {
        String editTitle = replace(title, placeholders), editSubTitle = replace(subTitle, placeholders);
        
        if (editTitle != null) {
            try {
                Object enumTitle = ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0]
                        .getField("TIMES").get(null);
                Object chatTitle = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                        .getMethod("a", String.class)
                        .invoke(null, "{\"text\":\"" + editTitle + "\"}");

                Constructor titleConstructor = ReflectionUtils.getNMSClass("PacketPlayOutTitle")
                        .getConstructor(ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                                ReflectionUtils.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);

                Object titlePacket = titleConstructor.newInstance(enumTitle, chatTitle, fadeIn, time, fadeOut);

                ReflectionUtils.sendPacket(player, titlePacket);

                enumTitle = ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0]
                        .getField("TITLE").get(null);

                chatTitle = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                        .getMethod("a", String.class)
                        .invoke(null, "{\"text\":\"" + editTitle + "\"}");

                titleConstructor = ReflectionUtils.getNMSClass("PacketPlayOutTitle")
                        .getConstructor(ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                                ReflectionUtils.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);

                titlePacket = titleConstructor.newInstance(enumTitle, chatTitle, fadeIn, time, fadeOut);

                ReflectionUtils.sendPacket(player, titlePacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (editSubTitle != null) {
            try {
                Object enumSubTitle = ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0]
                        .getField("TIMES").get(null);
                Object chatSubTitle = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                        .getMethod("a", String.class)
                        .invoke(null, "{\"text\":\"" + editSubTitle + "\"}");

                Constructor subTitleConstructor = ReflectionUtils.getNMSClass("PacketPlayOutTitle")
                        .getConstructor(ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                                ReflectionUtils.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);

                Object subTitlePacket = subTitleConstructor.newInstance(enumSubTitle, chatSubTitle, fadeIn, time, fadeOut);

                ReflectionUtils.sendPacket(player, subTitlePacket);

                enumSubTitle = ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0]
                        .getField("SUBTITLE").get(null);

                chatSubTitle = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                        .getMethod("a", String.class)
                        .invoke( null, "{\"text\":\"" + editSubTitle + "\"}");

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
        return title + "," + subTitle + "@" + path;
    }
}
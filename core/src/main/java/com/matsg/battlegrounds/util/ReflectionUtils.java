package com.matsg.battlegrounds.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static Object getField(Class<?> clazz, String fieldName, Object obj) {
        Field field;
        Object object = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            object = field.get(obj);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static EnumVersion getEnumVersion() {
        return EnumVersion.valueOf(getVersion().toUpperCase());
    }

    public static Class<?> getAnyClass(String name) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Class<?> getOBCClass(String name) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Class<?> getNMSClass(String name) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("net.minecraft.server." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    public static void sendPacket(Player player, Object packet) {
        Object craftPlayer, playerConnection;
        try {
            craftPlayer = player.getClass().getMethod("getHandle").invoke(player);
            playerConnection = craftPlayer.getClass().getField("playerConnection").get(craftPlayer);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

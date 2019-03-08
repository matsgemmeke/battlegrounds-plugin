package com.matsg.battlegrounds.nms;

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

    public enum EnumVersion {

        V1_8_R1("1.8.R1", 8),
        V1_8_R2("1.8.R2", 8),
        V1_8_R3("1.8.R3", 8),
        V1_9_R1("1.9.R1", 9),
        V1_9_R2("1.9.R2", 9),
        V1_10_R1("1.10.R1", 10),
        V1_11_R1("1.11.R1", 11),
        V1_12_R1("1.12.R1", 12),
        V1_13_R1("1.13.R1", 13),
        V1_13_R2("1.13.R2", 13);

        private int value;
        private String version;

        EnumVersion(String version, int value) {
            this.version = version;
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public String toString() {
            return version;
        }
    }
}

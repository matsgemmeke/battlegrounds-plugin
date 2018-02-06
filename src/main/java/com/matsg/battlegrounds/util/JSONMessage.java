package com.matsg.battlegrounds.util;

import com.matsg.battlegrounds.api.util.Message;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.ReflectionUtils.EnumVersion;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class JSONMessage implements Message {

    private EnumVersion version;
    private String command, hoverMessage, message;

    public JSONMessage(String message, String hoverMessage, String command) {
        this.message = message;
        this.hoverMessage = hoverMessage;
        this.command = command;
        this.version = ReflectionUtils.ENUM_VERSION;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(Placeholder... placeholders) {
        return replace(message, placeholders);
    }

    private String replace(String string, Placeholder... placeholders) {
        return Placeholder.replace(string, placeholders);
    }

    public void send(Player player, Placeholder... placeholders) {
        String editCommand = replace(command, placeholders),
                editHoverMessage = replace(hoverMessage, placeholders),
                editMessage = replace(message, placeholders);

        String text = "{\"text\":\"\",\"extra\":[{\"text\":\"" + editMessage + "\"," +
                "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + editHoverMessage + "\"}," +
                "\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + editCommand + "\"}}]}";

        try {
            Object jsonPacket;

            if (version.getValue() < 12) {
                Object icbc = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                        .getMethod("a", String.class).invoke(null, text);

                Constructor jsonConstructor = ReflectionUtils.getNMSClass("PacketPlayOutChat")
                        .getConstructor(ReflectionUtils.getNMSClass("IChatBaseComponent"), byte.class);

                jsonPacket = jsonConstructor.newInstance(icbc, (byte) 0);
            } else {
                Object chatMessageType = ReflectionUtils.getNMSClass("ChatMessageType").getField("CHAT").get(null);
                Object icbc = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                        .getMethod("a", String.class).invoke(null, text);

                Constructor jsonConstructor = ReflectionUtils.getNMSClass("PacketPlayOutChat")
                        .getConstructor(ReflectionUtils.getNMSClass("IChatBaseComponent"),
                                ReflectionUtils.getNMSClass("ChatMessageType"));

                jsonPacket = jsonConstructor.newInstance(icbc, chatMessageType);
            }

            ReflectionUtils.sendPacket(player, jsonPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
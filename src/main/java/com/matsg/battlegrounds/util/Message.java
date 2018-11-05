package com.matsg.battlegrounds.util;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.util.Placeholder;
import org.bukkit.ChatColor;

public class Message {

    private static Battlegrounds plugin = BattlegroundsPlugin.getPlugin();
    private static Translator translator = plugin.getTranslator();

    private static String applyPlaceholders(String message, Placeholder... placeholders) {
        for (Placeholder placeholder : placeholders) {
            if (message.contains("%" + placeholder.getIdentifier() + "%")) {
                message = placeholder.replace(message);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String create(String path, Placeholder... placeholders) {
        return applyPlaceholders(translator.getTranslation(path), placeholders);
    }

    public static String create(TranslationKey key, Placeholder... placeholders) {
        String message = translator.getTranslation(key.getPath());
        StringBuilder builder = new StringBuilder();
        if (key.hasPrefix()) {
            builder.append(translator.getTranslation(TranslationKey.PREFIX.getPath()));
        }
        for (Placeholder placeholder : placeholders) {
            if (message.contains("%" + placeholder.getIdentifier() + "%")) {
                message = placeholder.replace(message);
            }
        }
        builder.append(message);
        return ChatColor.translateAlternateColorCodes('&', builder.toString());
    }

    public static String createSimple(String message, Placeholder... placeholders) {
        return applyPlaceholders(message, placeholders);
    }
}

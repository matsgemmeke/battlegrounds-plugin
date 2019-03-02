package com.matsg.battlegrounds.util;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.util.Placeholder;
import org.bukkit.ChatColor;

import static com.matsg.battlegrounds.Translator.translate;

public class MessageHelper {

    private String applyPlaceholders(String message, Placeholder... placeholders) {
        for (Placeholder placeholder : placeholders) {
            if (message.contains("%" + placeholder.getIdentifier() + "%")) {
                message = placeholder.replace(message);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String create(String path, Placeholder... placeholders) {
        return applyPlaceholders(translate(path), placeholders);
    }

    public String create(TranslationKey key, Placeholder... placeholders) {
        String message = translate(key.getPath()), prefix = translate(TranslationKey.PREFIX.getPath());
        StringBuilder builder = new StringBuilder();
        if (prefix != null && key.hasPrefix()) {
            builder.append(prefix);
        }
        for (Placeholder placeholder : placeholders) {
            if (message.contains("%" + placeholder.getIdentifier() + "%")) {
                message = placeholder.replace(message);
            }
        }
        builder.append(message);
        return ChatColor.translateAlternateColorCodes('&', builder.toString());
    }

    public String createSimple(String message, Placeholder... placeholders) {
        return applyPlaceholders(message, placeholders);
    }
}

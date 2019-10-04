package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.storage.LanguageConfiguration;
import com.matsg.battlegrounds.api.storage.Yaml;
import org.bukkit.ChatColor;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class PluginTranslator implements Translator {

    public static final Locale FALLBACK_LOCALE = Locale.ENGLISH;

    private Locale locale;
    private Set<LanguageConfiguration> languageConfigurations;

    public PluginTranslator() {
        this.languageConfigurations = new HashSet<>();
    }

    public Set<LanguageConfiguration> getLanguageConfigurations() {
        return languageConfigurations;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    private LanguageConfiguration getLanguageConfiguration(Locale locale) {
        for (LanguageConfiguration lang : languageConfigurations) {
            if (lang.getLocale().equals(locale)) {
                return lang;
            }
        }
        return null;
    }

    public String createSimpleMessage(String message, Placeholder... placeholders) {
        return ChatColor.translateAlternateColorCodes('&', applyPlaceholders(message, placeholders));
    }

    public String translate(String key, Placeholder... placeholders) {
        LanguageConfiguration languageConfiguration = getLanguageConfiguration(locale);

        if (languageConfiguration == null) {
            handleInvalidTranslation(key);
        }

        String translation;
        Yaml yaml = languageConfiguration.getYaml();

        if ((translation = yaml.getString(key)) == null) {
            Yaml fallback = getLanguageConfiguration(FALLBACK_LOCALE).getYaml();

            if ((translation = fallback.getString(key)) == null) {
                handleInvalidTranslation(key);
            }
        }

        return ChatColor.translateAlternateColorCodes('&', applyPlaceholders(translation, placeholders));
    }

    private String applyPlaceholders(String message, Placeholder... placeholders) {
        for (Placeholder placeholder : placeholders) {
            if (message.contains("%" + placeholder.getIdentifier() + "%")) {
                message = placeholder.replace(message);
            }
        }
        return message;
    }

    private void handleInvalidTranslation(String key) {
        throw new TranslationNotFoundException("Translation for key \"" + key + "\" in lang " + locale.getCountry() + " not found");
    }
}

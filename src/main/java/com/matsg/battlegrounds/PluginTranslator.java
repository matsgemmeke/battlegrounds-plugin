package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.config.LanguageYaml;
import com.matsg.battlegrounds.api.config.Yaml;
import org.apache.commons.lang.LocaleUtils;

import java.io.IOException;
import java.util.Locale;

public class PluginTranslator implements Translator {

    private LanguageYaml[] languages;
    private Locale defaultLocale;

    public PluginTranslator(Battlegrounds plugin) throws IOException {
        this.defaultLocale = Locale.ENGLISH;
        this.languages = new LanguageYaml[] {
                new LanguageYaml(plugin, LocaleUtils.toLocale("en"))
        };
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    public LanguageYaml[] getLanguages() {
        return languages;
    }

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public LanguageYaml getLanguage(Locale locale) {
        for (LanguageYaml lang : languages) {
            if (lang.getLocale().equals(locale)) {
                return lang;
            }
        }
        return null;
    }

    public String getTranslation(String path) {
        if (getLanguage(defaultLocale) == null) {
            throw new TranslationNotFoundException("Translation \"" + path + "\" not found");
        }
        String string = getLanguage(defaultLocale).getString(path);
        if (string == null) {
            Yaml fallback = getLanguage(Locale.ENGLISH);
            if ((string = fallback.getString(path)) == null) {
                throw new TranslationNotFoundException("Translation \"" + path + "\" not found");
            }
        }
        return string;
    }
}
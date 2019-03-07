package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.LanguageYaml;
import com.matsg.battlegrounds.api.config.Yaml;
import org.apache.commons.lang.LocaleUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Translator {

    private static File languageDirectory;
    private static Locale locale, fallbackLocale = Locale.ENGLISH;
    private static Battlegrounds plugin = BattlegroundsPlugin.getPlugin();
    private static List<LanguageYaml> languages;

    public static File getLanguageDirectory() {
        return languageDirectory;
    }

    public static void setLanguageDirectory(File languageDirectory) {
        Translator.languageDirectory = languageDirectory;
    }

    public static List<LanguageYaml> getLanguages() {
        return languages;
    }

    public static void setLocale(String locale) {
        Translator.locale = LocaleUtils.toLocale(locale);
    }

    private static List<LanguageYaml> getLanguageList(File directory) {
        List<LanguageYaml> list = new ArrayList<>();

        // Look for language files
        try {
            File[] files = directory.listFiles();

            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (!file.isDirectory() && file.getName().startsWith("lang_")) {
                        Locale locale;

                        try {
                            locale = LocaleUtils.toLocale(file.getName().substring(5, file.getName().length() - 4));
                        } catch (NumberFormatException e) {
                            plugin.getLogger().severe("Could not read language file " + file.getName() + ": " + e.getMessage());
                            continue;
                        }

                        list.add(new LanguageYaml(plugin, locale));
                    }
                }
            } else {
                new LanguageYaml(plugin, Locale.ENGLISH); // Generate a new default language file
                return getLanguageList(languageDirectory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        plugin.getLogger().info("Succesfully loaded " + list.size() + " language file(s). Using " + locale.getDisplayLanguage(Locale.ENGLISH) + " as the default");
        return list;
    }

    private static LanguageYaml getLanguageYaml(Locale locale) {
        for (LanguageYaml lang : languages) {
            if (lang.getLocale().equals(locale)) {
                return lang;
            }
        }
        return null;
    }

    public static String translate(String path) {
        if (languages == null || languages.size() <= 0) {
            languages = getLanguageList(languageDirectory);
        }

        String translation;

        if (getLanguageYaml(locale) == null || (translation = getLanguageYaml(locale).getString(path)) == null) {
            Yaml fallback = getLanguageYaml(fallbackLocale);
            if ((translation = fallback.getString(path)) == null) {
                throw new TranslationNotFoundException("Translation \"" + path + "\" not found");
            }
        }

        return translation;
    }
}

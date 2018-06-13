package com.matsg.battlegrounds.api;

import com.matsg.battlegrounds.api.config.LanguageYaml;

import java.util.Locale;

public interface Translator {

    /**
     * Gets the default locale.
     *
     * @return The default locale
     */
    Locale getDefaultLocale();

    /**
     * Gets the language file which holds translations of a certain locale.
     *
     * @param locale The locale to find translations of
     * @return The language file which contains the translations of the locale
     */
    LanguageYaml getLanguage(Locale locale);

    /**
     * Gets all available language files
     *
     * @return All available language files
     */
    LanguageYaml[] getLanguages();

    /**
     * Gets the translation from the default language file. If it does not exist, a fallback language file will be used.
     *
     * @param path The path of the translation text
     * @return The translation from the default locale
     */
    String getTranslation(String path);

    /**
     * Sets the default locale.
     *
     * @param defaultLocale The default locale
     */
    void setDefaultLocale(Locale defaultLocale);
}
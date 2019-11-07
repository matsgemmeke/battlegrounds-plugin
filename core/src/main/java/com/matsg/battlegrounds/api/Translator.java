package com.matsg.battlegrounds.api;

import com.matsg.battlegrounds.api.storage.LanguageConfiguration;

import java.util.Collection;
import java.util.Locale;

public interface Translator {

    /**¡
     * Gets the available language configurations.
     *
     * @return the collection of language configurations
     */
    Collection<LanguageConfiguration> getLanguageConfigurations();

    /**
     * Gets the locale the translator is using.
     *
     * @return the translator locale
     */
    Locale getLocale();

    /**
     * Sets the locale the translator is using.
     *
     * @param locale the translator locale
     */
    void setLocale(Locale locale);

    /**
     * Creates a message where placeholders are applied.
     *
     * @param message the original message
     * @param placeholders optional placeholders for the message
     * @return the message with placeholders applied
     */
    String createSimpleMessage(String message, Placeholder... placeholders);

    /**
     * Gets the translation of a certain message by its translation key path.
     *
     * @param key the translation key
     * @param placeholders optional placeholders for the message
     * @return the translation message from the message path
     */
    String translate(String key, Placeholder... placeholders);
}

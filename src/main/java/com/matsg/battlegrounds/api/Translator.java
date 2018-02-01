package com.matsg.battlegrounds.api;

import com.matsg.battlegrounds.api.config.LanguageYaml;

import java.util.Locale;

public interface Translator {

    Locale getDefaultLocale();

    LanguageYaml getLanguage(Locale locale);

    LanguageYaml[] getLanguages();

    String getTranslation(String path);

    void setDefaultLocale(Locale defaultLocale);
}
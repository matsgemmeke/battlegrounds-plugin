package com.matsg.battlegrounds.api.config;

import com.matsg.battlegrounds.api.Battlegrounds;

import java.io.IOException;
import java.util.Locale;

public class LanguageYaml extends AbstractYaml {

    private Locale locale;

    public LanguageYaml(Battlegrounds plugin, Locale locale) throws IOException {
        super(plugin, plugin.getDataFolder().getPath() + "/lang", "lang_" + locale.getLanguage() + ".yml", false);
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}
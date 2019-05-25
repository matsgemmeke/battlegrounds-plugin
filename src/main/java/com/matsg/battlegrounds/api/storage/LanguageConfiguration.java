package com.matsg.battlegrounds.api.storage;

import java.util.Locale;

public class LanguageConfiguration {

    private final Locale locale;
    private final Yaml yaml;

    public LanguageConfiguration(Locale locale, Yaml yaml) {
        this.locale = locale;
        this.yaml = yaml;
    }

    public Locale getLocale() {
        return locale;
    }

    public Yaml getYaml() {
        return yaml;
    }
}

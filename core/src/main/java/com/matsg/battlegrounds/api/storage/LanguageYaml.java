package com.matsg.battlegrounds.api.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class LanguageYaml extends AbstractYaml {

    private Locale locale;

    public LanguageYaml(String filePath, InputStream resource, Locale locale) throws IOException {
        super("lang_" + locale.getLanguage() + ".yml", filePath + "/lang", resource, false);
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}

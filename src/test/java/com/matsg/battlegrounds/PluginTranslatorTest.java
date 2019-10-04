package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.storage.LanguageConfiguration;
import com.matsg.battlegrounds.api.storage.Yaml;
import org.bukkit.ChatColor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ChatColor.class)
public class PluginTranslatorTest {

    @Test
    public void createBasicTranslator() {
        PluginTranslator translator = new PluginTranslator();

        assertEquals(0, translator.getLanguageConfigurations().size());
        assertNull(translator.getLocale());
    }

    @Test
    public void changeTranslatorLocale() {
        PluginTranslator translator = new PluginTranslator();
        translator.setLocale(Locale.ENGLISH);

        assertEquals(Locale.ENGLISH, translator.getLocale());
    }

    @Test
    public void translateMessageWithPlaceholders() {
        String message = "Hello %placeholder%";
        Placeholder placeholder = new Placeholder("placeholder", "world");

        PluginTranslator translator = new PluginTranslator();
        String result = translator.createSimpleMessage(message, placeholder);

        assertEquals("Hello world", result);
    }

    @Test(expected = TranslationNotFoundException.class)
    public void translateMessageWithoutConfigurations() {
        Locale locale = Locale.ENGLISH;

        PluginTranslator translator = new PluginTranslator();
        translator.setLocale(locale);

        translator.translate("test-message-path");
    }

    @Test
    public void translateMessageWithKey() {
        Locale locale = Locale.ENGLISH;
        String key = "test-message-path";
        String testMessage = "Hello world";
        Yaml yaml = mock(Yaml.class);
        LanguageConfiguration languageConfiguration = new LanguageConfiguration(locale, yaml);

        when(yaml.getString(key)).thenReturn(testMessage);

        PluginTranslator translator = new PluginTranslator();
        translator.getLanguageConfigurations().add(languageConfiguration);
        translator.setLocale(locale);

        String result = translator.translate(key);

        assertEquals(testMessage, result);
    }

    @Test
    public void translateMessageUsingFallbackYaml() {
        Locale locale = Locale.GERMAN;
        String key = "test-message-path";
        String testMessage = "Hallo Welt";
        Yaml fallback = mock(Yaml.class);
        Yaml yaml = mock(Yaml.class);
        LanguageConfiguration languageConfiguration = new LanguageConfiguration(locale, yaml);
        LanguageConfiguration fallbackConfiguration = new LanguageConfiguration(PluginTranslator.FALLBACK_LOCALE, fallback);

        when(fallback.getString(key)).thenReturn(testMessage);
        when(yaml.getString(key)).thenReturn(null);

        PluginTranslator translator = new PluginTranslator();
        translator.getLanguageConfigurations().add(fallbackConfiguration);
        translator.getLanguageConfigurations().add(languageConfiguration);
        translator.setLocale(locale);

        String result = translator.translate(key);

        assertEquals(testMessage, result);
    }

    @Test(expected = TranslationNotFoundException.class)
    public void translateMessageInvalidKey() {
        Locale locale = Locale.GERMAN;
        String key = "test-message-path";
        Yaml fallback = mock(Yaml.class);
        Yaml yaml = mock(Yaml.class);
        LanguageConfiguration languageConfiguration = new LanguageConfiguration(locale, yaml);
        LanguageConfiguration fallbackConfiguration = new LanguageConfiguration(PluginTranslator.FALLBACK_LOCALE, fallback);

        when(fallback.getString(key)).thenReturn(null);
        when(yaml.getString(key)).thenReturn(null);

        PluginTranslator translator = new PluginTranslator();
        translator.getLanguageConfigurations().add(fallbackConfiguration);
        translator.getLanguageConfigurations().add(languageConfiguration);
        translator.setLocale(locale);

        translator.translate(key);
    }
}

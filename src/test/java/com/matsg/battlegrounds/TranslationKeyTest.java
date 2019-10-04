package com.matsg.battlegrounds;

import org.junit.Test;

import static org.junit.Assert.*;

public class TranslationKeyTest {

    @Test
    public void keyHasFileConfigurationPath() {
        TranslationKey key = TranslationKey.ALREADY_PLAYING;

        assertEquals("game-already-playing", key.getPath());
    }
}

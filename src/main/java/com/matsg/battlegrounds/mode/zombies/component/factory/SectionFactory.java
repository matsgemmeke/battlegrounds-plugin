package com.matsg.battlegrounds.mode.zombies.component.factory;

import com.matsg.battlegrounds.mode.zombies.component.Section;
import com.matsg.battlegrounds.mode.zombies.component.ZombiesSection;

public class SectionFactory {

    /**
     * Creates a section component based on the given input.
     *
     * @param id the component id
     * @param name the section name
     * @param price the price of the section
     * @param unlockedByDefault whether the section is unlocked at the start of a game
     * @return a section implementation
     */
    public Section make(int id, String name, int price, boolean unlockedByDefault) {
        return new ZombiesSection(id, name, price, unlockedByDefault);
    }
}

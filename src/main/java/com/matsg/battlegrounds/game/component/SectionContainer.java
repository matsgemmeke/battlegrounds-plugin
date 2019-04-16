package com.matsg.battlegrounds.game.component;

import com.matsg.battlegrounds.api.game.ComponentContainer;
import com.matsg.battlegrounds.api.game.Section;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SectionContainer implements ComponentContainer<Section> {

    private Set<Section> sections;

    public SectionContainer() {
        this.sections = new HashSet<>();
    }

    public void add(Section section) {
        sections.add(section);
    }

    public Section get(int id) {
        for (Section section : sections) {
            if (section.getId() == id) {
                return section;
            }
        }
        return null;
    }

    public Iterable<Section> getAll() {
        return Collections.unmodifiableSet(sections);
    }

    public void remove(int id) {
        Section section = get(id);
        if (section == null) {
            return;
        }
        sections.remove(section);
    }
}

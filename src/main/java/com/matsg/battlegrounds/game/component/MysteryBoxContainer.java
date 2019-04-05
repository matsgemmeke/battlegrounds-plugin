package com.matsg.battlegrounds.game.component;

import com.matsg.battlegrounds.api.game.ComponentContainer;
import com.matsg.battlegrounds.api.game.MysteryBox;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MysteryBoxContainer implements ComponentContainer<MysteryBox> {

    private Set<MysteryBox> mysteryBoxes;

    public MysteryBoxContainer() {
        this.mysteryBoxes = new HashSet<>();
    }

    public void add(MysteryBox mysteryBox) {
        mysteryBoxes.add(mysteryBox);
    }

    public MysteryBox get(int id) {
        for (MysteryBox mysteryBox : mysteryBoxes) {
            if (mysteryBox.getId() == id) {
                return mysteryBox;
            }
        }
        return null;
    }

    public Iterable<MysteryBox> getAll() {
        return Collections.unmodifiableSet(mysteryBoxes);
    }

    public void remove(MysteryBox mysteryBox) {
        mysteryBoxes.remove(mysteryBox);
    }
}

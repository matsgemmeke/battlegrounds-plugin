package com.matsg.battlegrounds.game.component;

import com.matsg.battlegrounds.api.game.ComponentContainer;
import com.matsg.battlegrounds.api.game.PerkMachine;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PerkMachineContainer implements ComponentContainer<PerkMachine> {

    private Set<PerkMachine> perkMachines;

    public PerkMachineContainer() {
        this.perkMachines = new HashSet<>();
    }

    public void add(PerkMachine perkMachine) {
        perkMachines.add(perkMachine);
    }

    public PerkMachine get(int id) {
        for (PerkMachine perkMachine : perkMachines) {
            if (perkMachine.getId() == id) {
                return perkMachine;
            }
        }
        return null;
    }

    public Iterable<PerkMachine> getAll() {
        return Collections.unmodifiableSet(perkMachines);
    }

    public void remove(PerkMachine perkMachine) {
        perkMachines.remove(perkMachine);
    }
}

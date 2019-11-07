package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.ArenaComponent;
import com.matsg.battlegrounds.api.game.ComponentContainer;

import java.util.*;

public class BattleComponentContainer<T extends ArenaComponent> implements ComponentContainer<T> {

    private Set<T> components;

    public BattleComponentContainer() {
        this.components = new HashSet<>();
    }

    public void add(T component) {
        components.add(component);
    }

    public T get(int id) {
        for (T component : components) {
            if (component.getId() == id) {
                return component;
            }
        }
        return null;
    }

    public Collection<T> getAll() {
        return Collections.unmodifiableSet(components);
    }

    public void remove(int id) {
        components.remove(get(id));
    }
}

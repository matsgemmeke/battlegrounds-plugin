package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.game.BattleComponentContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ZombiesSection implements Section {

    private boolean locked, unlockedByDefault;
    private ComponentContainer<Door> doorContainer;
    private ComponentContainer<ItemChest> itemChestContainer;
    private ComponentContainer<MobSpawn> mobSpawnContainer;
    private ComponentContainer<MysteryBox> mysteryBoxContainer;
    private ComponentContainer<PerkMachine> perkMachineContainer;
    private int id, price;
    private String name;

    public ZombiesSection(int id, String name, boolean unlockedByDefault) {
        this.id = id;
        this.name = name;
        this.unlockedByDefault = unlockedByDefault;
        this.doorContainer = new BattleComponentContainer<>();
        this.itemChestContainer = new BattleComponentContainer<>();
        this.locked = true;
        this.mobSpawnContainer = new BattleComponentContainer<>();
        this.mysteryBoxContainer = new BattleComponentContainer<>();
        this.perkMachineContainer = new BattleComponentContainer<>();
    }

    public int getId() {
        return id;
    }

    public ComponentContainer<Door> getDoorContainer() {
        return doorContainer;
    }

    public ComponentContainer<ItemChest> getItemChestContainer() {
        return itemChestContainer;
    }

    public ComponentContainer<MobSpawn> getMobSpawnContainer() {
        return mobSpawnContainer;
    }

    public ComponentContainer<MysteryBox> getMysteryBoxContainer() {
        return mysteryBoxContainer;
    }

    public String getName() {
        return name;
    }

    public ComponentContainer<PerkMachine> getPerkMachineContainer() {
        return perkMachineContainer;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isUnlockedByDefault() {
        return unlockedByDefault;
    }

    public ArenaComponent getComponent(int id) {
        for (ArenaComponent component : getComponents()) {
            if (component.getId() == id) {
                return component;
            }
        }
        return null;
    }

    public int getComponentCount() {
        int count = 0;
        for (ComponentContainer container : getContainers()) {
            count += container.getAll().size();
        }
        return count;
    }

    public Collection<ArenaComponent> getComponents() {
        List<ArenaComponent> list = new ArrayList<>();
        list.addAll(doorContainer.getAll());
        list.addAll(itemChestContainer.getAll());
        list.addAll(mobSpawnContainer.getAll());
        list.addAll(mysteryBoxContainer.getAll());
        list.addAll(perkMachineContainer.getAll());
        return list;
    }

    public <T extends ArenaComponent> Collection<T> getComponents(Class<T> componentClass) {
        List<T> list = new ArrayList<>();
        for (ArenaComponent component : getComponents()) {
            if (component.getClass().isAssignableFrom(componentClass)) {
                list.add((T) component);
            }
        }
        return Collections.unmodifiableList(list);
    }

    public boolean removeComponent(ArenaComponent component) {
        for (ComponentContainer container : getContainers()) {
            if (container.get(component.getId()) != null) {
                container.remove(component.getId());
                return true;
            }
        }
        return false;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;

        for (ComponentContainer<? extends Lockable> container : getContainers()) {
            for (Lockable component : container.getAll()) {
                component.setLocked(locked);
            }
        }
    }

    private ComponentContainer[] getContainers() {
        return new ComponentContainer[] { doorContainer, itemChestContainer, mobSpawnContainer, mysteryBoxContainer, perkMachineContainer };
    }
}

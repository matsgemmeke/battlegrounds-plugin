package com.matsg.battlegrounds.game.mode.zombies;

import com.matsg.battlegrounds.api.game.*;

import java.util.ArrayList;
import java.util.List;

public class ZombiesSection implements Section {

    private boolean locked;
    private ComponentContainer<Door> doorContainer;
    private ComponentContainer<ItemChest> itemChestContainer;
    private ComponentContainer<MobSpawn> mobSpawnContainer;
    private ComponentContainer<MysteryBox> mysteryBoxContainer;
    private ComponentContainer<PerkMachine> perkMachineContainer;
    private int id, price;
    private String name;

    public ZombiesSection(int id, String name) {
        this.id = id;
        this.name = name;
        this.doorContainer = new DoorContainer();
        this.itemChestContainer = new ItemChestContainer();
        this.locked = true;
        this.mobSpawnContainer = new MobSpawnContainer();
        this.mysteryBoxContainer = new MysteryBoxContainer();
        this.perkMachineContainer = new PerkMachineContainer();
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

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public ArenaComponent getComponent(int id) {
        for (ArenaComponent component : getComponents()) {
            if (component.getId() == id) {
                return component;
            }
        }
        return null;
    }

    public Iterable<ArenaComponent> getComponents() {
        List<ArenaComponent> list = new ArrayList<>();

        for (Door door : doorContainer.getAll()) {
            list.add(door);
        }
        for (ItemChest itemChest : itemChestContainer.getAll()) {
            list.add(itemChest);
        }
        for (MobSpawn mobSpawn : mobSpawnContainer.getAll()) {
            list.add(mobSpawn);
        }
        for (MysteryBox mysteryBox : mysteryBoxContainer.getAll()) {
            list.add(mysteryBox);
        }
        for (PerkMachine perkMachine : perkMachineContainer.getAll()) {
            list.add(perkMachine);
        }

        return list;
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

    private ComponentContainer[] getContainers() {
        return new ComponentContainer[] { doorContainer, itemChestContainer, mobSpawnContainer, mysteryBoxContainer, perkMachineContainer };
    }
}

package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.game.BattleComponentContainer;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ZombiesSection implements Section {

    private boolean locked, unlockedByDefault;
    private ComponentContainer<Barricade> barricadeContainer;
    private ComponentContainer<Door> doorContainer;
    private ComponentContainer<MobSpawn> mobSpawnContainer;
    private ComponentContainer<MysteryBox> mysteryBoxContainer;
    private ComponentContainer<PerkMachine> perkMachineContainer;
    private ComponentContainer<WallWeapon> wallWeaponContainer;
    private int id, price;
    private String name;

    public ZombiesSection(int id, String name, int price, boolean unlockedByDefault) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.unlockedByDefault = unlockedByDefault;
        this.barricadeContainer = new BattleComponentContainer<>();
        this.doorContainer = new BattleComponentContainer<>();
        this.locked = true;
        this.mobSpawnContainer = new BattleComponentContainer<>();
        this.mysteryBoxContainer = new BattleComponentContainer<>();
        this.perkMachineContainer = new BattleComponentContainer<>();
        this.wallWeaponContainer = new BattleComponentContainer<>();
    }

    public ComponentContainer<Barricade> getBarricadeContainer() {
        return barricadeContainer;
    }

    public ComponentContainer<Door> getDoorContainer() {
        return doorContainer;
    }

    public int getId() {
        return id;
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

    public ComponentContainer<WallWeapon> getWallWeaponContainer() {
        return wallWeaponContainer;
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

    public ArenaComponent getComponent(Location location) {
        for (Barricade barricade : barricadeContainer.getAll()) {
            if (barricade.contains(location)) {
                return barricade;
            }
        }
        for (Door door : doorContainer.getAll()) {
            if (door.contains(location)) {
                return door;
            }
        }
        for (MysteryBox mysteryBox : mysteryBoxContainer.getAll()) {
            if (mysteryBox.getLeftSide().getLocation().equals(location) || mysteryBox.getRightSide().getLocation().equals(location)) {
                return mysteryBox;
            }
        }
        for (PerkMachine perkMachine : perkMachineContainer.getAll()) {
            if (perkMachine.getSign().getLocation().equals(location)) {
                return perkMachine;
            }
        }
        for (WallWeapon wallWeapon : wallWeaponContainer.getAll()) {
            if (wallWeapon.getItemFrame().getLocation().getBlock().equals(location.getBlock())) {
                return wallWeapon;
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
        list.addAll(mobSpawnContainer.getAll());
        list.addAll(mysteryBoxContainer.getAll());
        list.addAll(perkMachineContainer.getAll());
        list.addAll(wallWeaponContainer.getAll());
        return Collections.unmodifiableList(list);
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
        return new ComponentContainer[] {
                barricadeContainer,
                doorContainer,
                mobSpawnContainer,
                mysteryBoxContainer,
                perkMachineContainer,
                wallWeaponContainer
        };
    }
}

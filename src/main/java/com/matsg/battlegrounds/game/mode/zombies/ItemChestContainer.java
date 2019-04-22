package com.matsg.battlegrounds.game.mode.zombies;

import com.matsg.battlegrounds.api.game.ComponentContainer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ItemChestContainer implements ComponentContainer<ItemChest> {

    private Set<ItemChest> itemChests;

    public ItemChestContainer() {
        this.itemChests = new HashSet<>();
    }

    public void add(ItemChest itemChest) {
        itemChests.add(itemChest);
    }

    public ItemChest get(int id) {
        for (ItemChest itemChest : itemChests) {
            if (itemChest.getId() == id) {
                return itemChest;
            }
        }
        return null;
    }

    public Iterable<ItemChest> getAll() {
        return Collections.unmodifiableSet(itemChests);
    }

    public void remove(int id) {
        ItemChest itemChest = get(id);
        if (itemChest == null) {
            return;
        }
        itemChests.remove(itemChest);
    }
}
